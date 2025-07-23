package org.utl.dovasystemcompose

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF4942CE)
    ) {
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.house),
                    contentDescription = "Inicio",
                    modifier = Modifier.size(36.dp)
                )
            },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.LightGray
            )
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.grafica),
                    contentDescription = "Gráfica",
                    modifier = Modifier.size(36.dp)
                )
            },
            selected = currentRoute == "grafica",
            onClick = { navController.navigate("grafica") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.LightGray
            )
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.bomba),
                    contentDescription = "Bomba",
                    modifier = Modifier.size(36.dp)
                )
            },
            selected = currentRoute == "bomba",
            onClick = { navController.navigate("bomba") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.LightGray
            )
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.temperatura),
                    contentDescription = "Temperatura",
                    modifier = Modifier.size(36.dp)
                )
            },
            selected = currentRoute == "temperatura",
            onClick = { navController.navigate("temperatura") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.LightGray
            )
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.historial),
                    contentDescription = "Historial",
                    modifier = Modifier.size(36.dp)
                )
            },
            selected = currentRoute == "historial",
            onClick = { navController.navigate("historial") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.LightGray
            )
        )
    }
}

