package org.utl.dovasystemcompose

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import org.utl.dovasystemcompose.viewModel.CaptacionViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import org.utl.dovasystemcompose.model.Captacion
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.net.Uri
import androidx.core.content.FileProvider
import android.util.Log
import android.content.ContentValues // ¡NUEVA IMPORTACIÓN!
import android.os.Build
import android.provider.MediaStore // ¡NUEVA IMPORTACIÓN!
import android.os.Environment // ¡NUEVA IMPORTACIÓN!
import androidx.annotation.RequiresApi


private const val TAG = "CSV_EXPORT"

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialCaptacionScreen(viewModel: CaptacionViewModel = viewModel()) {
    val context = LocalContext.current

    var selectedOption by remember { mutableStateOf("Fecha") }
    var inputValue by remember { mutableStateOf("") }

    val captacionesList by viewModel.captaciones.observeAsState(emptyList())
    val totalAguaCaptada by viewModel.totalAguaCaptada.observeAsState(0)

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
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Filtrar por:", modifier = Modifier.padding(end = 8.dp))
                    DropdownMenuBox(
                        options = listOf("Fecha", "Mes"),
                        selectedOption = selectedOption,
                        onOptionSelected = { selectedOption = it }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text(if (selectedOption == "Fecha") "Fecha (DD/MM/AAAA)" else "Mes (MM)") },
                    placeholder = { Text(if (selectedOption == "Fecha") "Ej: 15/07/2025" else "Ej: 07") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (inputValue.isNotBlank()) {
                                viewModel.cargarCaptaciones(selectedOption, inputValue)
                            } else {
                                Toast.makeText(context, "Por favor, ingresa un valor para filtrar.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A40C6))
                    ) {
                        Text("Generar Corte", color = Color.White)
                    }

                    Button(
                        onClick = {
                            if (captacionesList.isNotEmpty()) {
                                exportCaptacionesToCsv(context, captacionesList, selectedOption, inputValue)
                            } else {
                                Toast.makeText(context, "No hay datos para exportar.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text("Exportar CSV", color = Color.Black)
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
                        Text(
                            text = if (selectedOption == "Fecha") "Total del día: $inputValue" else "Total del mes: $inputValue",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$totalAguaCaptada L",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A40C6)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Registros Detallados:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (captacionesList.isEmpty()) {
                    Text("No hay registros para el criterio seleccionado.", color = Color.Gray)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(captacionesList) { captacion ->
                            CaptacionItem(captacion = captacion)
                        }
                    }
                }
            }
        }
    }
}

// =======================================================================
// === FUNCIÓN PARA EXPORTAR A CSV (MODIFICADA PARA GUARDAR DIRECTO) ===
// =======================================================================
@RequiresApi(Build.VERSION_CODES.Q)
private fun exportCaptacionesToCsv(
    context: Context,
    captaciones: List<Captacion>,
    criterio: String,
    valor: String
) {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "captaciones_${criterio}_${valor.replace("/", "-")}_$timestamp.csv"
    val csvHeader = "Fecha,Hora,AguaCaptada (L)\n"

    Log.d(TAG, "Intentando guardar CSV en Descargas: $fileName")

    try {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // Nombre del archivo visible para el usuario
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")   // Tipo MIME del archivo
            // Directorio donde se guardará (Descargas)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context.contentResolver
        // Inserta un nuevo elemento en la colección de Descargas y obtén su URI
        val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let { fileUri ->
            // Abre un OutputStream para escribir datos en la URI obtenida
            resolver.openOutputStream(fileUri)?.use { outputStream ->
                outputStream.write(csvHeader.toByteArray())
                captaciones.forEach { captacion ->
                    val line = "${captacion.fecha},${captacion.hora},${captacion.aguaCaptada}\n"
                    outputStream.write(line.toByteArray())
                }
                Log.d(TAG, "CSV guardado exitosamente en: $fileUri")
                Toast.makeText(context, "CSV guardado en la carpeta Descargas: $fileName", Toast.LENGTH_LONG).show()
            } ?: run {
                Log.e(TAG, "No se pudo abrir el stream para escribir el archivo.")
                Toast.makeText(context, "Error: No se pudo abrir el stream para guardar el archivo.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Log.e(TAG, "No se pudo crear la URI para el archivo en Descargas.")
            Toast.makeText(context, "Error: No se pudo crear el archivo en Descargas.", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error general al guardar CSV: ${e.message}", e)
        Toast.makeText(context, "Error al guardar CSV: ${e.message}", Toast.LENGTH_LONG).show()
    }
}


// Composable para cada item de la lista de captaciones (sin cambios)
@Composable
fun CaptacionItem(captacion: Captacion) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.gota),
                contentDescription = "Agua",
                tint = Color(0xFF4A40C6),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Fecha: ${captacion.fecha}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "Hora: ${captacion.hora}", fontSize = 14.sp, color = Color.Gray)
            }
            Text(
                text = "${captacion.aguaCaptada} L",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A40C6)
            )
        }
    }
}


// DropdownMenuBox (sin cambios)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4A40C6),
                unfocusedBorderColor = Color.Gray
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}