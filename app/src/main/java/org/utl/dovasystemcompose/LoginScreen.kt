package org.utl.dovasystemcompose

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth // Import Firebase Auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() } // Get Firebase Auth instance

    var usuario by remember { mutableStateOf("") } // This will be the email for Firebase Auth
    var contrasena by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4942CE))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.save),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contenedor inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .background(
                        color = Color(0xFF64D5F4),
                        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4942CE),
                        modifier = Modifier.padding(top = 12.dp, bottom = 20.dp)
                    )

                    // Campo Usuario (Email)
                    OutlinedTextField(
                        value = usuario,
                        onValueChange = { usuario = it },
                        placeholder = { Text("Correo electrónico") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(bottom = 12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    // Campo Contraseña
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        placeholder = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(bottom = 12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    // Botón Login
                    Button(
                        onClick = {
                            if (usuario.isNotBlank() && contrasena.isNotBlank()) {
                                auth.signInWithEmailAndPassword(usuario, contrasena)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(context, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                                            // *** CORRECTED LINE HERE ***
                                            context.startActivity(Intent(context, Dashboard::class.java)) // Redirect to your Dashboard
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(context, "Error en el inicio de sesión: usuario o contraseña incorrectos.", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A40C6),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Link to Registration (Optional but highly recommended)
                    TextButton(
                        onClick = {
                            context.startActivity(Intent(context, Registro::class.java))
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(
                            text = "¿No tienes una cuenta? Regístrate aquí",
                            color = Color(0xFF4942CE),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}