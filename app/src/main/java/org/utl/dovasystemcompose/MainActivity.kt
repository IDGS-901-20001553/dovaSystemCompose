package org.utl.dovasystemcompose

import android.os.Bundle
import androidx.activity.ComponentActivity // <-- FALTA ESTE IMPORT
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import org.utl.dovasystemcompose.ui.theme.DovaSystemComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DovaSystemComposeTheme {
                SplashScreen()
            }
        }
    }
}
