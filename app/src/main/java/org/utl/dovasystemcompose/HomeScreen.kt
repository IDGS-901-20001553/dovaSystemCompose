package org.utl.dovasystemcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import org.utl.dovasystemcompose.viewModel.DashboardViewModel
import androidx.compose.runtime.getValue


@Composable
fun HomeScreen(viewModel: DashboardViewModel = viewModel()) {
    val estadoBomba by viewModel.estadoBomba.observeAsState("0") // Por defecto estara apagada

    val historial by viewModel.historial.observeAsState(emptyList())
    GraficaAguaVico(niveles = historial)

    //llenado dinamico cisterna
    val nivelActualMl by viewModel.nivelAgua.observeAsState(0)
    val porcentajeLlenado = (nivelActualMl / 2500.0 * 100).toInt().coerceIn(0, 100)

    val textoBomba = if (estadoBomba == "1") "Encendida" else "Apagada"
    val colorBomba = if (estadoBomba == "1") Color(0xFF4CAF50) else Color.Red

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A40C6)) // Fondo morado
    ) {
        // Header
        DashboardHeader(
            imageRes = R.drawable.house, // Asegúrate que exista en drawable
            userName = "Juan Pérez"        // O dinámico si es login real
        )
        // Contenedor blanco con esquinas redondeadas
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
            ) {
                // MONITOREO USUARIO
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 70.dp, top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                }

                // TÍTULO MONITOREO
                Text(
                    text = "Monitoreo en Tiempo Real",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .padding(horizontal = 25.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )

                // grafica en tiempo real
                GraficaAguaVico(niveles = historial)


                // ESTADO ACTUAL DE LA CISTERNA
                Text(
                    text = "Estado actual de la cisterna",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 90.dp, top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cisterna),
                        contentDescription = null,
                        modifier = Modifier.size(width = 130.dp, height = 80.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "$porcentajeLlenado%",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                // SECCIÓN DE ALERTAS
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEDEDED))
                        .padding(vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 25.dp)
                    ) {
                        // Mostrar la alerta de llenado solo si el porcentaje ≥ 80%
                        if (porcentajeLlenado >= 80) {
                            CardAlerta(
                                iconId = R.drawable.alerta,
                                title = "Alerta de Llenado",
                                status = "$porcentajeLlenado%",
                                color = Color(0xFF3F51B5)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }

                        // Siempre mostrar el estado de la bomba
                        CardAlerta(
                            iconId = R.drawable.estado,
                            title = "Bomba de Agua",
                            status = textoBomba,
                            color = colorBomba
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


