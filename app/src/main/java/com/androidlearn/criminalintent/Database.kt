package com.androidlearn.criminalintent

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidlearn.criminalintent.features.crime.data.Crime
import com.androidlearn.criminalintent.features.crime.data.CrimeDao
import com.androidlearn.criminalintent.features.crime.data.CrimeTypeConverters

@Database(
    entities = [ Crime::class ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ],
    version=2
)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao

}
