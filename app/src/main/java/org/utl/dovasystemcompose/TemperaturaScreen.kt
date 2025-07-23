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
fun TemperaturaScreen() {
    var selectedOption by remember { mutableStateOf("Fecha") }
    var inputValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A40C6))
    ) {
        HeaderSection(imageRes = R.drawable.termometros, title = "Temperatura del agua")

        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hoy",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFA8F58C))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ter),
                        contentDescription = "Termómetro",
                        modifier = Modifier.size(width = 50.dp, height = 100.dp)
                    )
                    Text(
                        text = "20 °C",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                LeyendaColores()

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    RadioButtonWithLabel(
                        selected = selectedOption == "Fecha",
                        text = "Fecha",
                        onClick = { selectedOption = "Fecha" }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButtonWithLabel(
                        selected = selectedOption == "Mes",
                        text = "Mes",
                        onClick = { selectedOption = "Mes" }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        placeholder = {
                            Text(
                                if (selectedOption == "Fecha") "dd/mm/aaaa" else "mm"
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { /* Acción de búsqueda */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A40C6))
                    ) {
                        Text("Buscar", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Fecha y hora                   Temperatura",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("28/05/2025 11:00              -  12°C")
                    Text("21/05/2025 11:00              -  26°C")
                }
            }
        }
    }
}

@Composable
fun LeyendaColores() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(bottom = 8.dp, start = 20.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                modifier = Modifier.padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF3A81F0))
                )
                Text(
                    text = " Fría <15°",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp, end = 15.dp, bottom = 15.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFA8F58C))
                )
                Text(
                    text = " Bien 16°-22°",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                modifier = Modifier.padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFD93D))
                )
                Text(
                    text = " Tibia 23°-30°",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFF05B5B))
                )
                Text(
                    text = " Alta >30°",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

