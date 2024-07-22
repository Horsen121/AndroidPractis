package com.androidlearn.criminalintent

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.androidlearn.criminalintent.features.crime.CrimeScreen
import com.androidlearn.criminalintent.features.crimes.CrimesScreen
import com.androidlearn.criminalintent.ui.theme.CriminalIntentTheme
import com.androidlearn.criminalintent.utils.Routes

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CriminalIntentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val viewModel: MainViewModel by lazy { MainViewModel(applicationContext) }

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.CrimesScreen.route
                    ) {
                        composable(Routes.MainScreen.route) {
                            MainScreen()
                        }
                        composable(Routes.CrimesScreen.route) {
                            CrimesScreen(
                                context = applicationContext,
                                navController = navController
                            )
                        }
                        composable(
                            Routes.CrimeScreen.route+ "?crimeId={crimeId}",
                            arguments = listOf(
                                navArgument(name = "crimeId") {
                                    type = NavType.LongType
                                    defaultValue = -1L
                                },
                            )
                        ) {
                            CrimeScreen(
                                context = applicationContext,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen() {

    }
}