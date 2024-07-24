package com.androidlearn.criminalintent

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidlearn.criminalintent.features.crime.data.Crime
import com.androidlearn.criminalintent.features.crime.data.CrimeDao
import com.androidlearn.criminalintent.features.crime.data.CrimeTypeConverters

@Database(
    entities = [ Crime::class ],
    exportSchema = true,
    version=1
)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao


}
