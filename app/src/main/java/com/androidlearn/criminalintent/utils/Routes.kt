package com.androidlearn.criminalintent.utils

sealed class Routes(val route: String) {
    data object MainScreen: Routes("main_screen")
    data object CrimesScreen: Routes("crimes_screen")
    data object CrimeScreen: Routes("crime_screen")
}