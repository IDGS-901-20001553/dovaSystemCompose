package org.utl.dovasystemcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.utl.dovasystemcompose.model.Captacion
import org.utl.dovasystemcompose.MqttManager
import org.utl.dovasystemcompose.viewModel.CaptacionViewModel


@Composable
fun GraficaCaptacionScreen() {
    var selectedOption by remember { mutableStateOf("Fecha") }
    var textValue by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    val viewModel: CaptacionViewModel = viewModel()
    val captaciones by viewModel.captaciones.observeAsState(emptyList())

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
                // Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    RadioButtonWithLabel(
                        selected = selectedOption == "Fecha",
                        text = "Fecha",
                        onClick = {
                            selectedOption = "Fecha"
                            errorMsg = ""
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButtonWithLabel(
                        selected = selectedOption == "Mes",
                        text = "Mes",
                        onClick = {
                            selectedOption = "Mes"
                            errorMsg = ""
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Entrada
                OutlinedTextField(
                    value = textValue,
                    onValueChange = {
                        textValue = it
                        errorMsg = ""
                    },
                    placeholder = {
                        Text(if (selectedOption == "Fecha") "dd/mm/aaaa" else "mm")
                    },
                    isError = errorMsg.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val valor = textValue.trim()
                        val fechaRegex = Regex("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$")
                        val mesRegex = Regex("^(0[1-9]|1[0-2])$")

                        if (valor.isEmpty()) {
                            errorMsg = "Por favor ingresa un valor."
                        } else if (selectedOption == "Fecha" && !fechaRegex.matches(valor)) {
                            errorMsg = "Formato de fecha inválido. Usa dd/mm/aaaa."
                        } else if (selectedOption == "Mes" && !mesRegex.matches(valor)) {
                            errorMsg = "Formato de mes inválido. Usa mm."
                        } else {
                            errorMsg = ""
                            viewModel.cargarCaptaciones(selectedOption, valor)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A40C6))
                ) {
                    Text("Buscar", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (errorMsg.isNotEmpty()) {
                    Text(errorMsg, color = Color.Red)
                } else if (captaciones.isNotEmpty()) {
                    GraficaBarrasCaptacion(captaciones)
                } else {
                    Text("Sin datos aún.")
                }
            }
        }
    }
}


