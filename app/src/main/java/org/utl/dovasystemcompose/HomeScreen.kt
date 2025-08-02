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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen(userEmail: String, onLogout: () -> Unit, viewModel: DashboardViewModel = viewModel()) {
    val estadoBomba by viewModel.estadoBomba.observeAsState("0")
    val historial by viewModel.historial.observeAsState(emptyList())
    val nivelActualMl by viewModel.nivelAgua.observeAsState(0)
    val porcentajeLlenado = (nivelActualMl / 2500.0 * 100).toInt().coerceIn(0, 100)

    val textoBomba = if (estadoBomba == "1") "Encendido" else "Apagado"
    val colorBomba = if (estadoBomba == "1") Color.Green else Color.Red


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A40C6)) // Fondo morado
    ) {
        // Header
        DashboardHeader(
            imageRes = R.drawable.house,
            userName = userEmail, // Pasa el userEmail
            onLogoutClick = onLogout // Pasa la función onLogout
        )

        // Contenedor blanco con esquinas redondeadas (el resto de tu diseño de HomeScreen)
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Sección de Nivel de Agua
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mundogr),
                        contentDescription = "Icono de Agua",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Nivel de agua",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF4A40C6)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.gota),
                        contentDescription = "Icono de Gota",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "$porcentajeLlenado%",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                // *** GRÁFICA MOVIDA AQUÍ, ANTES DE LA SECCIÓN DE ALERTAS ***
                Spacer(modifier = Modifier.height(16.dp)) // Espacio antes de la gráfica
                GraficaAguaVico(niveles = historial)
                Spacer(modifier = Modifier.height(16.dp)) // Espacio después de la gráfica

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
                        if (porcentajeLlenado >= 80) {
                            CardAlerta(
                                iconId = R.drawable.alerta,
                                title = "Alerta de Llenado",
                                status = "$porcentajeLlenado%",
                                color = Color(0xFF3F51B5)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }

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