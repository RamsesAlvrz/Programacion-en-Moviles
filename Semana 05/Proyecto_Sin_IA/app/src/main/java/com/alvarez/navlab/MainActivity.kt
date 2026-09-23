package com.alvarez.navlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alvarez.navlab.navigation.AppNavigation
import com.alvarez.navlab.ui.theme.NavLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavLabTheme(dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}