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

    private val _estadoBomba = MutableLiveData("Apagada")
    val estadoBomba: LiveData<String> = _estadoBomba

    private val _modoControl = MutableLiveData("AUTOMÁTICO")
    val modoControl: LiveData<String> = _modoControl

    private val _historialBomba = MutableLiveData<List<HistorialBomba>>(emptyList())
    val historialBomba: LiveData<List<HistorialBomba>> = _historialBomba

    init {
        // Conectar y suscribirse solo a estado-bomba
        mqttManager.connect()
        // Suscripción al estado de la bomba
        mqttManager.estadoBomba.observeForever { estado ->
            // Traducir estado '1' o '0' a texto visible
            val textoEstado = if (estado == "1") "Encendida" else "Apagada"
            _estadoBomba.postValue(textoEstado)
        }

        // Cargar historial
        cargarHistorialBomba()
    }

    fun encenderBomba() {
        mqttManager.publishBombaCommand("1")
    }

    fun apagarBomba() {
        mqttManager.publishBombaCommand("0")
    }

    fun cambiarModoControl() {
        val nuevoModo = if (_modoControl.value == "AUTOMÁTICO") "MANUAL" else "AUTOMÁTICO"
        _modoControl.value = nuevoModo
        mqttManager.publishBombaCommand(if (nuevoModo == "AUTOMÁTICO") "auto" else "manual")
    }

    fun cargarHistorialBomba() {
        viewModelScope.launch {
            mqttManager.observarHistorialBombaTiempoReal(
                onDataChange = { historial ->
                    _historialBomba.postValue(historial)
                },
                onError = {
                    // Opcional manejo de error
                }
            )
        }
    }
}
