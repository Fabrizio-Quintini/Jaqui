
package com.example.jaqui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jaqui.ui.screens.FormularioScreen
import com.example.jaqui.ui.screens.LoginScreen
import com.example.jaqui.ui.screens.MemoriaScreen
import com.example.jaqui.ui.screens.MenuScreen
import com.example.jaqui.ui.screens.QuizScreen
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
                },
                onRegistroClick = {
                    navController.navigate("registro")
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

        composable("registro") {
            FormularioScreen(
                viewModel = viewModel(),
                onRegistroExitoso = {
                    navController.navigate("menu") {
                        popUpTo("inicio") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {
            LoginScreen(
                viewModel = viewModel(),
                onLoginExitoso = {
                    navController.navigate("menu") {
                        popUpTo("inicio") { inclusive = true }
                    }
                },
                onRegistroClick = {
                    navController.navigate("registro")
                }
            )
        }
        composable("tarjetas") {
            TarjetasScreen()
        }

        composable("quiz") {
            QuizScreen()
        }

        composable("memoria") {
            MemoriaScreen(
                viewModel = viewModel(),
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
