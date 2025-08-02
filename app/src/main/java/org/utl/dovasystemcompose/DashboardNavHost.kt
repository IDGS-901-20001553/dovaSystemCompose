package org.utl.dovasystemcompose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController


@Composable
fun DashboardNavHost(userName: String, onLogout: () -> Unit) { // Aceptar 'onLogout'
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pasar 'onLogout' a HomeScreen
            composable("home") { HomeScreen(userEmail = userName, onLogout = onLogout) }
            composable("grafica") { GraficaCaptacionScreen() }
            composable("bomba") { ModuloBombaScreen() }
            composable("temperatura") { TemperaturaScreen() }
            composable("historial") { HistorialCaptacionScreen() }
        }
    }
}