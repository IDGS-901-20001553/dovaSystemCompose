package org.utl.dovasystemcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp // Importar el icono de salir
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // Importar IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale

@Composable
fun DashboardHeader(
    imageRes: Int,
    userName: String,
    onLogoutClick: () -> Unit // Nuevo parámetro para la acción de logout
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp), // Ajustado a 24.dp para consistencia con HomeScreen
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "User Icon",
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) { // Añadir weight para que el texto ocupe el espacio y el botón se alinee al final
            Text(
                text = "¡Bienvenido!",
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = userName,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Botón de cerrar sesión
        IconButton(onClick = onLogoutClick) {
            Icon(
                imageVector = Icons.Filled.ExitToApp, // Icono de "salir"
                contentDescription = "Cerrar Sesión",
                tint = Color.White, // Color del icono
                modifier = Modifier.size(30.dp)
            )
        }
    }
}