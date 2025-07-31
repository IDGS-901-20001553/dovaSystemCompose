package org.utl.dovasystemcompose.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.utl.dovasystemcompose.MqttManager
import org.utl.dovasystemcompose.model.Captacion

class CaptacionViewModel : ViewModel() {
    private val _captaciones = MutableLiveData<List<Captacion>>(emptyList())
    val captaciones: LiveData<List<Captacion>> = _captaciones

    // Nuevo LiveData para el total de agua captada calculado
    private val _totalAguaCaptada = MutableLiveData<Int>(0)
    val totalAguaCaptada: LiveData<Int> = _totalAguaCaptada

    private val mqttManager = MqttManager()

    fun cargarCaptaciones(criterio: String, valor: String) {
        mqttManager.leerCaptacionesDesdeFirebase(
            criterio,
            valor,
            onSuccess = { listaFiltrada ->
                _captaciones.postValue(listaFiltrada) // Actualiza la lista de registros detallados

                // Calcula el total según el criterio
                val calculatedTotal = if (criterio == "Mes") {
                    // Agrupa por día, encuentra el máximo de aguaCaptada por día, y luego suma esos máximos
                    listaFiltrada
                        .groupBy { it.fecha } // Agrupar por fecha
                        .values // Obtener colecciones de captaciones por fecha
                        .sumOf { captacionesDelDia ->
                            captacionesDelDia.maxOfOrNull { it.aguaCaptada } ?: 0
                        }
                } else {
                    // Para "Fecha" (o cualquier otro criterio), suma directamente todos los valores
                    listaFiltrada.sumOf { it.aguaCaptada }
                }
                _totalAguaCaptada.postValue(calculatedTotal) // Actualiza el total calculado
            },
            onError = { /* maneja errores si quieres */ }
        )
    }
}