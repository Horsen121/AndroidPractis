package com.androidlearn.criminalintent.features.crime.view.crimes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlearn.criminalintent.features.crime.data.Crime
import com.androidlearn.criminalintent.features.crime.data.CrimeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CrimesViewModel : ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    var crimes = mutableStateOf( listOf<Crime>() )

    private var getDataJob: Job? = null
    init {
        getData()
    }

    private fun getData() {
        getDataJob?.cancel()
        getDataJob = crimeRepository.getCrimes()
            .onEach {
                crimes.value = it
            }
            .launchIn(viewModelScope)
    }
}