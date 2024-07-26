package com.androidlearn.criminalintent.features.crime.view.addEditCrime

import android.content.Context
import android.net.Uri
import android.text.format.DateFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.androidlearn.criminalintent.R
import com.androidlearn.criminalintent.features.crime.data.Crime
import com.androidlearn.criminalintent.features.crime.data.CrimeRepository
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

private const val DATE_FORMAT = "EEE, MMM, dd"

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
    var suspect by mutableStateOf("")
    var date by mutableStateOf(Date())
    var photo by mutableStateOf<Uri?>(null)
    var isSolved by mutableStateOf(false)
    private var crime: Crime = Crime()

    init {
        savedStateHandle.get<Long>("crimeId")?.let { crimeId ->
            if (crimeId != -1L) {
                viewModelScope.launch {
                     crimeRepository.getCrime(crimeId)?.also {
                         crime = it
                         title = it.title
                         suspect = it.suspect
                         photo = getPhotoFile().toUri()
                         date = it.date
                         isSolved = it.isSolved
                    }
                }
            }
        }
    }

    fun getPhotoFile(): File {
        return crimeRepository.getPhotoFile(crime)
    }
    fun getUri(context: Context): Uri {
        return FileProvider.getUriForFile(
            context,
            "com.androidlearn.criminalintent.fileprovider",
            getPhotoFile()
        )
    }

    fun getCrimeReport(context: Context): String {
        val solvedString = if (crime.isSolved) {
            context.getString(R.string.crime_report_solved)
        } else {
            context.getString(R.string.crime_report_unsolved)
        }
        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        val suspect = if (crime.suspect.isBlank()) {
            context.getString(R.string.crime_report_no_suspect)
        } else {
            context.getString(R.string.crime_report_suspect, crime.suspect)
        }
        return context.getString(R.string.crime_report, crime.title, dateString, solvedString, suspect)
    }
    fun saveCrime() {
        viewModelScope.launch {
            crimeRepository.editCrime(
                crime.copy(
                    title = title,
                    date = date,
                    suspect = suspect,
                    isSolved = isSolved
                )
            )
        }
    }
}

