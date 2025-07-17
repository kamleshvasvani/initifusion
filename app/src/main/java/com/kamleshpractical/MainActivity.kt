package com.kamleshpractical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kamleshpractical.navigation.Screen
import com.kamleshpractical.screens.LoginScreen
import com.kamleshpractical.screens.NotesScreen
import com.kamleshpractical.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setContent {
                val navController = rememberNavController()
                NavHostController(navController = navController)
            }
        }
    }
}

@Composable
fun NavHostController(navController: NavHostController = rememberNavController()) {
    val viewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.NoteScreen.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.NoteScreen.route) {
            NotesScreen()
        }
    }
}

