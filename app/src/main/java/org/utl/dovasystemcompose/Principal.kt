package org.utl.dovasystemcompose

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight


@Composable
fun PantallaPrincipal() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.gota),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(250.dp)
                    .height(285.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Contenedor inferior redondeado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 28.dp,
                            topEnd = 28.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(Color(0xFF64D5F4))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Botón Login
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, Login::class.java))
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A40C6))
                    ) {
                        Text(
                            text = "Login",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Botón Registro
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, Registro::class.java))
                        },
                        modifier = Modifier.width(200.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64D5F4)),
                        border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text(
                            text = "Registrate",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


