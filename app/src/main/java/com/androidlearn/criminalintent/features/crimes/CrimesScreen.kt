package com.androidlearn.criminalintent.features.crimes

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.androidlearn.criminalintent.utils.Routes

@Composable
fun CrimesScreen(
    context: Context,
    navController: NavController,
    viewModel: CrimesViewModel = viewModel()
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(viewModel.crimes) { crime ->
            CrimeCard(
                crime = crime,
                onClick = {
                    navController.navigate(Routes.CrimeScreen.route + "?crimeId=${crime.id}")
                }
            )
        }
    }
}