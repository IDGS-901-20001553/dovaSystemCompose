package org.utl.dovasystemcompose

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth // Import Firebase Auth

@Composable
fun SplashScreen() {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() } // Get Firebase Auth instance

    LaunchedEffect(Unit) {
        delay(3000) // Shorten delay for testing if needed
        val nextActivity = if (auth.currentUser != null) {
            // User is signed in
            PantallaPrincipalActivity::class.java
        } else {
            // No user is signed in
            Login::class.java
        }
        context.startActivity(Intent(context, nextActivity))
        // Optionally finish the splash screen activity to prevent going back to it
        // if (context is ComponentActivity) {
        //     context.finish()
        // }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.main),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}