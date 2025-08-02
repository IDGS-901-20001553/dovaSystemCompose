package org.utl.dovasystemcompose

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase // Importar FirebaseDatabase
import com.google.firebase.database.database // Importar la extensión 'database'
import com.google.firebase.Firebase // Importar Firebase principal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen() {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val database = remember { Firebase.database } // Obtener la instancia de Realtime Database

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4942CE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.mundogr),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Contenedor de formulario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFF64D5F4),
                        shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "¡Regístrate!",
                    fontSize = 26.sp,
                    color = Color(0xFF4942CE),
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 20.dp)
                )

                // Campo Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Correo
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo Contraseña
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Registrarse
                Button(
                    onClick = {
                        if (correo.isNotBlank() && contrasena.isNotBlank() && nombre.isNotBlank()) {
                            auth.createUserWithEmailAndPassword(correo, contrasena)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Registro de autenticación exitoso
                                        val userId = auth.currentUser?.uid
                                        if (userId != null) {
                                            // Guardar el nombre en Realtime Database usando el UID
                                            val usersRef = database.getReference("users") // Referencia a una colección 'users'
                                            usersRef.child(userId).child("nombre").setValue(nombre)
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Registro exitoso y nombre guardado.", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(context, Login::class.java)
                                                    context.startActivity(intent)
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(context, "Registro exitoso, pero error al guardar nombre: ${e.message}", Toast.LENGTH_LONG).show()
                                                    val intent = Intent(context, Login::class.java)
                                                    context.startActivity(intent)
                                                }
                                        } else {
                                            Toast.makeText(context, "Registro exitoso, pero UID de usuario no encontrado.", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(context, Login::class.java)
                                            context.startActivity(intent)
                                        }
                                    } else {
                                        // Si falla el registro de autenticación
                                        Toast.makeText(context, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4942CE),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Registrarse", fontSize = 18.sp)
                }
            }
        }
    }
}