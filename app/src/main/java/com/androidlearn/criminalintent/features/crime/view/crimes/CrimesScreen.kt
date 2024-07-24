package com.androidlearn.criminalintent.features.crime.view.crimes

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        items(viewModel.crimes.value) { crime ->
            CrimeCard(
                crime = crime,
                onClick = {
                    navController.navigate(Routes.CrimeScreen.route + "?crimeId=${crime.id}")
                }
            )
        }
    }
}