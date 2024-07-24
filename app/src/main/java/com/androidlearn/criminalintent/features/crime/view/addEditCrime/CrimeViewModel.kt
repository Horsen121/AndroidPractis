package com.androidlearn.criminalintent.features.crime.view.addEditCrime

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.androidlearn.criminalintent.features.crime.data.Crime
import com.androidlearn.criminalintent.features.crime.data.CrimeRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date

class CrimeViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel()  {
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()

                return CrimeViewModel(
                    savedStateHandle
                ) as T
            }
        }
    }

    private val crimeRepository = CrimeRepository.get()
    var title by mutableStateOf("")
    var date by mutableStateOf(Date())
    var isSolved by mutableStateOf(false)
    private var crime: Crime? = null

    init {
        savedStateHandle.get<Long>("crimeId")?.let { crimeId ->
            if (crimeId != -1L) {
                viewModelScope.launch {
                     crimeRepository.getCrime(crimeId)?.also {
                         crime = it
                         title = it.title
                         date = it.date
                         isSolved = it.isSolved
                    }
                }
            }
        }
    }

    fun saveCrime() {
        viewModelScope.launch {
            crime?.let {
                crimeRepository.editCrime(
                    it.copy(
                        title = title,
                        date = date,
                        isSolved = isSolved
                    )
                )
            } ?: crimeRepository.addCrime(
                Crime(
                    title = title,
                    date = date,
                    isSolved = isSolved
                )
            )
        }
    }
}

