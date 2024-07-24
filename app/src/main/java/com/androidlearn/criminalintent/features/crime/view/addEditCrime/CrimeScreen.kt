package com.androidlearn.criminalintent.features.crime.view.addEditCrime

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.androidlearn.criminalintent.R

@Composable
fun CrimeScreen(
    context: Context,
    navController: NavController,
    viewModel: CrimeViewModel = viewModel(
        modelClass = CrimeViewModel::class.java,
        factory = CrimeViewModel.Factory
    )
) {
    BackHandler(viewModel.title.isNotEmpty()) {
        viewModel.saveCrime()
        navController.navigateUp()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        Text(text = stringResource(id = R.string.crime_title_label))
        TextField(
            value = viewModel.title,
            onValueChange = { viewModel.title = it },
            placeholder = { Text(text = stringResource(R.string.crime_title_hint)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {

        }) {
            Text(text = viewModel.date.toString())
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.isSolved,
                onCheckedChange = { viewModel.isSolved = it },
                modifier = Modifier.padding(8.dp)
            )
            Text(text = stringResource(id = R.string.crime_solved_label))
        }
    }
}