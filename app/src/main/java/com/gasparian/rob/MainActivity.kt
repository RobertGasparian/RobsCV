package com.gasparian.rob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gasparian.rob.core.dsm.theme.RcvTheme
import com.gasparian.rob.navigation.ui.RcvAppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RcvTheme {
                RcvAppNavigation(
                    onExit = ::finish,
                )
            }
        }
    }
}
