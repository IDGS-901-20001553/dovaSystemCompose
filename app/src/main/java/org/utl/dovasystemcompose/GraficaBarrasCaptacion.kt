package org.utl.dovasystemcompose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import org.utl.dovasystemcompose.model.Captacion

@Composable
fun GraficaBarrasCaptacion(captaciones: List<Captacion>) {
    if (captaciones.isEmpty()) return

    val datosOrdenados = captaciones.sortedBy { it.hora }

    val entryModel = entryModelOf(
        *datosOrdenados.mapIndexed { index, it ->
            index to it.aguaCaptada.toFloat()
        }.toTypedArray()
    )

    Chart(
        chart = columnChart(), // Sin personalizar columnas directamente
        model = entryModel,
        startAxis = rememberStartAxis(), // Eje Y visible
        bottomAxis = rememberBottomAxis(), // Eje X visible
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(16.dp)
    )
}
