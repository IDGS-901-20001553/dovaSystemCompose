package org.utl.dovasystemcompose.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.utl.dovasystemcompose.MqttManager
import org.utl.dovasystemcompose.model.Captacion

class CaptacionViewModel : ViewModel() {
    private val _captaciones = MutableLiveData<List<Captacion>>(emptyList())
    val captaciones: LiveData<List<Captacion>> = _captaciones

    private val mqttManager = MqttManager()

    fun cargarCaptaciones(criterio: String, valor: String) {
        mqttManager.leerCaptacionesDesdeFirebase(
            criterio,
            valor,
            onSuccess = { _captaciones.postValue(it) },
            onError = { /* maneja errores si quieres */ }
        )
    }
}