package org.utl.dovasystemcompose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf


import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis




@Composable
fun GraficaAguaVico(niveles: List<Int>) {
    if (niveles.isEmpty()) return
    val chart = lineChart()

    val entries = niveles.mapIndexed { index, ml ->
        FloatEntry(index.toFloat(), ml.toFloat())
    }

    val entryModel = entryModelOf(entries)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 16.dp)
    ) {
        Chart(
            chart = chart,
            model = entryModel,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis()
        )
    }
}






