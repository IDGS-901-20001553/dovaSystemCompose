package org.utl.dovasystemcompose

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import org.utl.dovasystemcompose.model.Captacion
import org.utl.dovasystemcompose.model.HistorialBomba
import java.text.SimpleDateFormat
import java.util.*

class MqttManager {
    private val _temperature = MutableLiveData<String>()
    val temperature: LiveData<String> = _temperature

    //lista observable para los puntos de la grafica nivel de agua
    private val _nivelAgua = MutableLiveData<Int>()
    val nivelAgua: LiveData<Int> = _nivelAgua

    private val database = Firebase.database("https://dovasystemcompose-default-rtdb.firebaseio.com")
    private val tempRef = database.getReference("temperatura")
    private val usersRef = database.getReference("users")
    private val historialBombaRef = database.getReference("historialBomba")

    private val mqttClient = MqttClient.builder()
        .useMqttVersion5()
        .serverHost("0700dea795744ea78d630750a0dc059e.s1.eu.hivemq.cloud")
        .serverPort(8883)
        .sslWithDefaultConfig()
        .identifier("android-client-${System.currentTimeMillis()}")
        .buildAsync()

    fun connect() {
        mqttClient.connectWith()
            .simpleAuth()
            .username("junoNyu")
            .password("Iot123456".toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    Log.e("MQTT", "Error al conectar: ${throwable.message}")
                } else {
                    Log.i("MQTT", "Conectado exitosamente a HiveMQ")
                    subscribeToTemperature()
                    subscribeToEstadoBomba()
                    subscribeToSensorUltrasonico()
                }
            }
    }
    fun getUserName(uid: String, onComplete: (String?) -> Unit) {
        usersRef.child(uid).child("nombre").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userName = snapshot.getValue(String::class.java)
                onComplete(userName)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseDB", "Error al leer el nombre del usuario: ${error.message}")
                onComplete(null)
            }
        })
    }

    private fun subscribeToTemperature() {
        mqttClient.subscribeWith()
            .topicFilter("sensorTemperatura")
            .qos(MqttQos.AT_LEAST_ONCE)
            .callback { publish ->
                val buffer = publish.payload.get()
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val temp = String(bytes, Charsets.UTF_8)

                Log.d("MQTT", "Mensaje recibido: $temp")
                _temperature.postValue(temp)
                guardarEnFirebase(temp)
            }
            .send()
    }

    //guarda temperatura en firebase cada minuto
    private var ultimaGuardada: Long = 0L // Almacena el tiempo en milisegundos

    private fun guardarEnFirebase(temperatura: String) {
        val valorNumerico = temperatura.toDoubleOrNull()
        if (valorNumerico != null) {
            val ahora = System.currentTimeMillis()

            // Verifica si han pasado al menos 60 segundos (60,000 ms)
            if (ahora - ultimaGuardada >= 60_000) {
                val fechaHoraActual = Calendar.getInstance().time
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formatoHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

                val entrada = mapOf(
                    "fecha" to formatoFecha.format(fechaHoraActual),
                    "hora" to formatoHora.format(fechaHoraActual),
                    "valor" to valorNumerico
                )

                tempRef.push().setValue(entrada)
                ultimaGuardada = ahora // Actualiza el tiempo del último guardado

                Log.d("MQTT", "Dato guardado en Firebase: $valorNumerico")
            } else {
                Log.d("MQTT", "Ignorado: aún no ha pasado un minuto desde el último guardado.")
            }
        } else {
            Log.e("MQTT", "Error: valor recibido '$temperatura' no es numérico")
        }
    }


    //leer estado de la bomba desde el mqtt
    private val _estadoBomba = MutableLiveData<String>()
    val estadoBomba: LiveData<String> = _estadoBomba

    private fun subscribeToEstadoBomba() {
        mqttClient.subscribeWith()
            .topicFilter("estado-bomba")
            .qos(MqttQos.AT_LEAST_ONCE)
            .callback { publish ->
                val buffer = publish.payload.get()
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val estado = String(bytes, Charsets.UTF_8)

                Log.d("MQTT", "Estado de bomba recibido: $estado")
                _estadoBomba.postValue(estado)
            }
            .send()
    }


    //funcion para calcular distancia

    fun distanciaACantidad(distancia: Double): Int {
        val alturaTotal = 12.0 // cm
        val alturaLlenado = (alturaTotal - distancia).coerceIn(0.0, alturaTotal)
        val volumenMaximo = 2500.0
        val volumen = (alturaLlenado / alturaTotal) * volumenMaximo
        return volumen.toInt()
    }

    //calculo de captacion y datos mqtt para graficar
    private val captacionRef = database.getReference("captacion")
    private var ultimaCaptacionGuardada: Long = 0L

    fun subscribeToSensorUltrasonico() {
        mqttClient.subscribeWith()
            .topicFilter("sensorUltrasonico")
            .qos(MqttQos.AT_LEAST_ONCE)
            .callback { publish ->
                val buffer = publish.payload.get()
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val distanciaStr = String(bytes, Charsets.UTF_8)
                val distancia = distanciaStr.toDoubleOrNull()

                distancia?.let {
                    val ml = distanciaACantidad(it)
                    _nivelAgua.postValue(ml)
                    guardarCaptacionEnFirebase(ml)
                }
            }
            .send()
    }

    private fun guardarCaptacionEnFirebase(ml: Int) {
        val ahora = System.currentTimeMillis()
        if (ahora - ultimaCaptacionGuardada >= 5 * 60_000) {
            val fechaHora = Calendar.getInstance().time
            val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formatoHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            val entrada = Captacion(
                fecha = formatoFecha.format(fechaHora),
                hora = formatoHora.format(fechaHora),
                aguaCaptada = ml
            )

            captacionRef.push().setValue(entrada)
            ultimaCaptacionGuardada = ahora
        }
    }

    //funcion grafica de captacion toma datos de firebase de la tabla captacion
    fun leerCaptacionesDesdeFirebase(
        criterio: String,
        valor: String,
        onSuccess: (List<Captacion>) -> Unit,
        onError: (String) -> Unit
    ) {
        val ref = Firebase.database("https://dovasystemcompose-default-rtdb.firebaseio.com")
            .getReference("captacion")

        ref.get().addOnSuccessListener { snapshot ->
            val lista = snapshot.children.mapNotNull { it.getValue(Captacion::class.java) }

            val filtradas = when (criterio) {
                "Fecha" -> lista.filter { it.fecha == valor }
                "Mes" -> lista.filter {
                    val partes = it.fecha.split("/")
                    partes.size == 3 && partes[1] == valor
                }
                else -> emptyList()
            }

            onSuccess(filtradas)
        }.addOnFailureListener {
            onError("Error al leer captaciones: ${it.message}")
        }
    }

    fun publishBombaCommand(command: String) {
        val topic = "control-bomba"
        Log.d("MQTT", "Publicando comando de bomba: $command en $topic")
        mqttClient.publishWith().topic(topic).qos(MqttQos.AT_LEAST_ONCE).payload(command.toByteArray()).send()
    }

    fun publishBombaMode(mode: String) {
        val topic = "modo-bomba"
        Log.d("MQTT", "Publicando modo de bomba: $mode en $topic")
        mqttClient.publishWith().topic(topic).qos(MqttQos.AT_LEAST_ONCE).payload(mode.toByteArray()).send()
    }

    // Nueva función para suscribirse al estado de la bomba
    fun subscribeToBombaEstado(onMessageReceived: (String) -> Unit) {
        val topic = "estado-bomba"
        mqttClient.subscribeWith()
            .topicFilter(topic)
            .qos(MqttQos.AT_LEAST_ONCE)
            .callback { publish ->
                val estado = String(publish.payloadAsBytes)
                Log.d("MQTT", "Mensaje de estado de bomba recibido: $estado")
                onMessageReceived(estado)
            }
            .send()
    }

    // Nueva función para suscribirse al modo de control de la bomba
    fun subscribeToBombaModo(onMessageReceived: (String) -> Unit) {
        val topic = "modo-bomba"
        mqttClient.subscribeWith()
            .topicFilter(topic)
            .qos(MqttQos.AT_LEAST_ONCE)
            .callback { publish ->
                val modo = String(publish.payloadAsBytes)
                Log.d("MQTT", "Mensaje de modo de bomba recibido: $modo")
                onMessageReceived(modo)
            }
            .send()
    }

    // Nueva función para leer el historial de la bomba desde Firebase
    fun leerHistorialBombaDesdeFirebase(
        onSuccess: (List<HistorialBomba>) -> Unit,
        onError: (String) -> Unit
    ) {
        historialBombaRef.get().addOnSuccessListener { snapshot ->
            val lista = snapshot.children.mapNotNull { it.getValue(HistorialBomba::class.java) }
            onSuccess(lista)
        }.addOnFailureListener {
            onError("Error al leer historial de bomba: ${it.message}")
        }
    }

    fun disconnect() {
        mqttClient.disconnect()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    Log.e("MQTT", "Error al desconectar: ${throwable.message}")
                } else {
                    Log.i("MQTT", "Desconectado correctamente")
                }
            }
    }
}