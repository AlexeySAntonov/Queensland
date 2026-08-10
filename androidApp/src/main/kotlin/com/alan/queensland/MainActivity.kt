package com.alan.queensland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alan.queensland.di.DI
import com.alan.queensland.ui.QueenslandApp

class MainActivity : ComponentActivity() {
    private val appComponent by lazy { DI.appComponent }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            QueenslandApp(appComponent = appComponent)
        }
    }
}
