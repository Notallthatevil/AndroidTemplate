package com.example.template.ux.systemui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.template.ux.main.Screen
import com.example.template.ui.composable.AppTopAppBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SystemUiScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    Scaffold(topBar = { AppTopAppBar(title = Screen.SYSTEM_UI.title, onBack = navController::popBackStack) }) {
    }

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
        )
        systemUiController.setStatusBarColor(color = Color.Cyan)
        systemUiController.setNavigationBarColor(color = Color.Magenta)
    }
}