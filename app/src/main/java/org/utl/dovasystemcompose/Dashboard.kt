package org.utl.dovasystemcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth

class Dashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val firebaseAuth = remember { FirebaseAuth.getInstance() }
            val mqttManager = remember { MqttManager() }

            var userName by remember { mutableStateOf("Cargando...") }

            LaunchedEffect(firebaseAuth.currentUser) {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    mqttManager.getUserName(currentUser.uid) { name ->
                        userName = name ?: "Usuario"
                    }
                } else {
                    userName = "Invitado"
                }
            }

            // Definir la función de logout aquí
            val onLogout: () -> Unit = {
                firebaseAuth.signOut() // Cerrar sesión de Firebase
                // Navegar de vuelta a la pantalla de Login
                context.startActivity(Intent(context, Login::class.java))
                finish() // Finalizar la actividad actual del Dashboard
            }

            // Pasar el userName y la función onLogout al DashboardNavHost
            DashboardNavHost(userName = userName, onLogout = onLogout)
        }
    }
}