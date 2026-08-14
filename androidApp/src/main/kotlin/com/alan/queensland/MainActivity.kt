package com.alan.queensland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alan.queensland.di.DI
import com.alan.queensland.ui.QueenslandApp

class MainActivity : ComponentActivity() {
    private val appComponent by lazy { DI.appComponent }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QueenslandApp(appComponent = appComponent)
        }
    }
}
