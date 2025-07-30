package org.utl.dovasystemcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

@Composable
fun ModuloBombaScreen() {
    var isAutoMode by remember { mutableStateOf(true) }

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
                Image(
                    painter = painterResource(id = R.drawable.bomb),
                    contentDescription = "Bomba",
                    modifier = Modifier.size(200.dp)
                )

                // Botones ON y OFF
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { /* Encender lógica */ },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66f122))
                    ) {
                        Text("ON", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { /* Apagar lógica */ },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                    ) {
                        Text("OFF", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Botón AUTO / MANUAL centrado abajo
                Button(
                    onClick = { isAutoMode = !isAutoMode },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .heightIn(min = 48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text(
                        text = if (isAutoMode) "AUTO" else "MANUAL",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Historial
                Text(
                    text = "Historial de Encendidos",
                    modifier = Modifier.padding(top = 24.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text("Fecha y Hora                Estado", fontWeight = FontWeight.Bold)
                Text("14/07/2025 12:10         Encendida")
                Text("14/07/2025 11:45         Apagada")
            }
        }
    }
}


