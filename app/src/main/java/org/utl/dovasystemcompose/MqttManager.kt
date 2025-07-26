package org.utl.dovasystemcompose

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import java.text.SimpleDateFormat
import java.util.*

class MqttManager {
    private val _temperature = MutableLiveData<String>()
    val temperature: LiveData<String> = _temperature

    private val database = Firebase.database("https://dovasystemcompose-default-rtdb.firebaseio.com")
    private val tempRef = database.getReference("temperatura")

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
                }
            }
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

