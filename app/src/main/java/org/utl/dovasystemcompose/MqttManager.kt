package org.utl.dovasystemcompose

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos

class MqttManager {

    private val _temperature = MutableLiveData<String>()
    val temperature: LiveData<String> = _temperature

    // Reemplaza con tu configuración
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
            }
            .send()
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

