package com.androidlearn.criminalintent.features.crime.view.crimes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.androidlearn.criminalintent.features.crime.data.Crime

@Composable
fun CrimeCard(
    crime: Crime,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = crime.title)
            Text(text = crime.date.toString())
        }
        Image(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Green),
            modifier = Modifier.alpha(if (crime.isSolved) 1f else 0f)
        )
    }
    HorizontalDivider()
}