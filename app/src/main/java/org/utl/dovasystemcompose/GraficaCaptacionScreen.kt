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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraficaCaptacionScreen() {
    var selectedOption by remember { mutableStateOf("Fecha") }
    var textValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A40C6))
    ) {
        HeaderSection(imageRes = R.drawable.agua, title = "Gráfica de captación")

        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Selector de opciones
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButtonWithLabel(
                        selected = selectedOption == "Fecha",
                        text = "Fecha",
                        onClick = { selectedOption = "Fecha" }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    RadioButtonWithLabel(
                        selected = selectedOption == "Mes",
                        text = "Mes",
                        onClick = { selectedOption = "Mes" }
                    )
                }

                // Campo de entrada con placeholder según selección
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    placeholder = {
                        Text(
                            text = if (selectedOption == "Fecha") "10/05/2025" else "05"
                        )
                    },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(380.dp),
                    singleLine = true
                )

                Text(
                    text = "Litros captados por mes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp),
                    color = Color.Black
                )

                Image(
                    painter = painterResource(id = R.drawable.gra),
                    contentDescription = "Gráfica",
                    modifier = Modifier
                        .height(380.dp)
                        .padding(10.dp)
                        .size(350.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}


