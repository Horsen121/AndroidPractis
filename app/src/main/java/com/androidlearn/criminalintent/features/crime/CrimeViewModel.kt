package com.androidlearn.criminalintent.features.crime

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch
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

    var title by mutableStateOf<String?>(null)
    var date by mutableStateOf(Date())
    var isSolved by mutableStateOf(false)

    init {
        savedStateHandle.get<Long>("crimeId")?.let { crimeId ->
            if (crimeId != -1L) {
                viewModelScope.launch {
                        // get crime by id
                }
            }
        }
    }
}

