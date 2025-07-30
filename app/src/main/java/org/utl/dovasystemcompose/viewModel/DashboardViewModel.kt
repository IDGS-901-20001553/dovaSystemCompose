package org.utl.dovasystemcompose.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.utl.dovasystemcompose.MqttManager


class DashboardViewModel  : ViewModel() {
    private val mqttManager = MqttManager()

    val estadoBomba: LiveData<String> = mqttManager.estadoBomba
    val nivelAgua = mqttManager.nivelAgua

    private val _historial = MutableLiveData<List<Int>>(emptyList())
    val historial: LiveData<List<Int>> = _historial

    init {
        mqttManager.connect()
        nivelAgua.observeForever { nuevoNivel ->
            val actual = _historial.value ?: emptyList()
            val actualizado = (actual + nuevoNivel).takeLast(20)
            _historial.postValue(actualizado)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mqttManager.disconnect()
    }
}