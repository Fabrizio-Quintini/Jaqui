
package com.example.jaqui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jaqui.ui.screens.MenuScreen
import com.example.jaqui.ui.screens.TarjetasScreen
import com.example.jaqui.ui.screens.TraductorScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio",
        modifier = modifier
    ) {
        composable("inicio") {
            TraductorScreen(
                onInvitadoClick = {
                    navController.navigate("menu")
                },
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("menu") {
            MenuScreen(
                onTarjetasClick = {
                    navController.navigate("tarjetas")
                },
                onQuizClick =  {
                    navController.navigate("quiz")
                },
                onMemoriaClick = {
                    navController.navigate("memoria")
            }
            )
        }

        composable("login") {
            Text(text = "Inicio de sesión: próximamente")
        }
        composable("tarjetas") {
            TarjetasScreen()
        }

        composable("quiz") {
            Text(text = "Cuestionario")
        }

        composable("memoria") {
            Text(text = "Juego de memoria")
        }
    }
}
