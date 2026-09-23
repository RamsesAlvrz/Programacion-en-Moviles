package com.alvarez.navlabia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alvarez.navlabia.navigation.AppNavigation
import com.alvarez.navlabia.ui.theme.NavLabIATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavLabIATheme(dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}