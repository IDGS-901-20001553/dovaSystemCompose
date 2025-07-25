package org.utl.dovasystemcompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class TemperaturaViewModel : ViewModel() {
    private val mqttManager = MqttManager()
    val temperatura: LiveData<String> = mqttManager.temperature

    init {
        mqttManager.connect()
    }

    override fun onCleared() {
        super.onCleared()
        mqttManager.disconnect()
    }
}
