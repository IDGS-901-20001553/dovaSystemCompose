package org.utl.dovasystemcompose.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.utl.dovasystemcompose.MqttManager
import org.utl.dovasystemcompose.model.HistorialBomba
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ModuloBombaViewModel : ViewModel() {
    private val mqttManager = MqttManager()

    private val _estadoBomba = MutableLiveData<String>("Apagada") // 'Encendida' o 'Apagada'
    val estadoBomba: LiveData<String> = _estadoBomba

    private val _modoControl = MutableLiveData<String>("AUTOMÁTICO") // 'AUTOMÁTICO' o 'MANUAL'
    val modoControl: LiveData<String> = _modoControl

    private val _historialBomba = MutableLiveData<List<HistorialBomba>>(emptyList())
    val historialBomba: LiveData<List<HistorialBomba>> = _historialBomba

    init {
        // Conectar al broker MQTT al inicializar el ViewModel
        mqttManager.connect()
        // Suscribirse a los tópicos relevantes
        mqttManager.subscribeToBombaEstado { estado ->
            _estadoBomba.postValue(estado)
        }
        mqttManager.subscribeToBombaModo { modo ->
            _modoControl.postValue(modo)
        }
        // Cargar el historial desde Firebase al iniciar
        cargarHistorialBomba()
    }

    fun encenderBomba() {
        mqttManager.publishBombaCommand("1")
    }

    fun apagarBomba() {
        mqttManager.publishBombaCommand("0")
    }

    fun cambiarModoControl() {
        // Alternar entre AUTOMÁTICO y MANUAL y enviar el comando MQTT
        val nuevoModo = if (_modoControl.value == "AUTOMÁTICO") "MANUAL" else "AUTOMÁTICO"
        mqttManager.publishBombaMode(if (nuevoModo == "AUTOMÁTICO") "1" else "0")
    }

    fun cargarHistorialBomba() {
        viewModelScope.launch {
            mqttManager.leerHistorialBombaDesdeFirebase(
                onSuccess = { historial ->
                    _historialBomba.postValue(historial.sortedByDescending { it.fecha + it.hora })
                },
                onError = { /* manejar error */ }
            )
        }
    }
}