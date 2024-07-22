package com.androidlearn.criminalintent.features.crimes

import androidx.lifecycle.ViewModel
import com.androidlearn.criminalintent.features.crime.Crime

class CrimesViewModel(): ViewModel() {
    val crimes = mutableListOf<Crime>()

    init {
        for (i in 0 until 10) {
            val crime = Crime(
                title = "Crime #$i",
                isSolved = i % 2 == 0
            )

            crimes += crime
        }
    }

}