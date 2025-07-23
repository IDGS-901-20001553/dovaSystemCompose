package org.utl.dovasystemcompose


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController


@Composable
fun DashboardNavHost() {
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
            composable("home") { HomeScreen() }
            composable("grafica") { GraficaCaptacionScreen() }
            composable("bomba") { ModuloBombaScreen() }
            composable("temperatura") { TemperaturaScreen() }
            composable("historial") { HistorialCaptacionScreen() }
        }
    }
}
