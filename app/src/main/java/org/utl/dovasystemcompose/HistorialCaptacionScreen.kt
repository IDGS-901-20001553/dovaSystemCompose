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

@Composable
fun HistorialCaptacionScreen() {
    var selectedOption by remember { mutableStateOf("Fecha") }
    var inputValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A40C6))
    ) {
        HeaderSection(imageRes = R.drawable.captacion, title = "Historial de Captación")

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Historial", fontSize = 20.sp)
                    Button(
                        onClick = { /* export CSV */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A40C6))
                    ) {
                        Text("Exportar CSV", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .height(165.dp)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    items(getHistorialMock()) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.fechaHora)
                                Text("${item.litros} L")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Corte Captación", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(modifier = Modifier.width(244.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButtonWithLabel(
                                    selected = selectedOption == "Fecha",
                                    text = "Fecha",
                                    onClick = { selectedOption = "Fecha" }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = inputValue,
                                    onValueChange = { inputValue = it },
                                    placeholder = { Text("dd/mm/aaaa") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButtonWithLabel(
                                    selected = selectedOption == "Mes",
                                    text = "Mes",
                                    onClick = { selectedOption = "Mes" }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedTextField(
                                    value = inputValue,
                                    onValueChange = { inputValue = it },
                                    placeholder = { Text("Mes (1-12)") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Button(
                            onClick = { /* generar corte */ },
                            modifier = Modifier.padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A40C6))
                        ) {
                            Text("Generar Corte", color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Corte del día: 14/07/2025", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("25 L", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A40C6))
                    }
                }
            }
        }
    }
}


