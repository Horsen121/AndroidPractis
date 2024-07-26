package com.androidlearn.criminalintent.features.crime.view.crimes

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.androidlearn.criminalintent.R
import com.androidlearn.criminalintent.utils.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrimesScreen(
    context: Context,
    navController: NavController,
    viewModel: CrimesViewModel = viewModel()
) {
    Scaffold(
        topBar = { TopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {
                IconButton(onClick = { navController.navigate(Routes.CrimeScreen.route) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        )}
    ) {
        if (viewModel.crimes.value.isEmpty()) {
            Text(text = stringResource(id = R.string.crimes_emptylist))
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 72.dp)
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
    }
}