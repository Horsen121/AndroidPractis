package com.androidlearn.criminalintent

import android.app.Application
import com.androidlearn.criminalintent.features.crime.data.CrimeRepository

class CriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}
