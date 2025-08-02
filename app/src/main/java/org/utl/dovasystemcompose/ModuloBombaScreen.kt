package org.utl.dovasystemcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import org.utl.dovasystemcompose.model.HistorialBomba
import org.utl.dovasystemcompose.viewModel.ModuloBombaViewModel

@Composable
fun ModuloBombaScreen(viewModel: ModuloBombaViewModel = viewModel()) {
    // Observar el estado desde el ViewModel
    val estadoBomba by viewModel.estadoBomba.observeAsState("Apagada")
    val modoControl by viewModel.modoControl.observeAsState("AUTOMÁTICO")
    val historial by viewModel.historialBomba.observeAsState(emptyList())

    val estadoColor = if (estadoBomba == "Encendida") Color.Green else Color.Red
    val modoBotonColor = if (modoControl == "AUTOMÁTICO") Color(0xFF0288D1) else Color(0xFFFFA000)
    val modoBotonText = if (modoControl == "AUTOMÁTICO") "MODO AUTOMÁTICO" else "MODO MANUAL"
    val onOffEnabled = modoControl == "MANUAL"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A40C6))
    ) {
        HeaderSection(imageRes = R.drawable.bomb2, title = "Módulo Bomba de Agua")

        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen de la bomba (restaurada)
                Image(
                    painter = painterResource(id = R.drawable.bomb),
                    contentDescription = "Bomba",
                    modifier = Modifier.size(200.dp)
                )

                // Estado actual de la bomba
                Text(
                    text = "Estado Actual:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = estadoBomba,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = estadoColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Botones ON / OFF (ahora funcionales)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { viewModel.encenderBomba() },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp),
                        enabled = onOffEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66f122))
                    ) {
                        Text("ON", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.apagarBomba() },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp),
                        enabled = onOffEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                    ) {
                        Text("OFF", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Botón AUTO / MANUAL (ahora funcional)
                Button(
                    onClick = { viewModel.cambiarModoControl() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .heightIn(min = 48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = modoBotonColor)
                ) {
                    Text(
                        text = modoBotonText,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Historial (restaurado y ahora dinámico)
                Text(
                    text = "Historial de Encendidos/Apagados",
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Lista de historial (usando LazyColumn para eficiencia)
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(historial) { registro ->
                        HistorialBombaItem(registro)
                    }
                }
            }
        }
    }
}

// Composable para cada item del historial (sin cambios)
@Composable
fun HistorialBombaItem(registro: HistorialBomba) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color = if (registro.estado == "Encendida") Color.Green else Color.Red
            Icon(
                painter = painterResource(id = R.drawable.gota), // Ícono relevante para el estado
                contentDescription = registro.estado,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${registro.fecha} ${registro.hora}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = registro.estado,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}