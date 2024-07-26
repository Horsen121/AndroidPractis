package com.androidlearn.criminalintent.features.crime.data

import android.content.Context
import androidx.room.Room
import com.androidlearn.criminalintent.CrimeDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime_db.db"

class CrimeRepository private constructor(context: Context) {
    companion object {
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    )
        .createFromAsset("crime_db.db")
        .build()

    private val crimeDao = database.crimeDao()
    private val filesDir = context.applicationContext.filesDir
    private val executor = Executors.newSingleThreadExecutor()

    fun getCrimes(): Flow<List<Crime>> = crimeDao.getCrimes()
    suspend fun getCrime(id: Long): Crime? = crimeDao.getCrime(id).first()
    fun addCrime(crime: Crime) = executor.execute { crimeDao.insertCrime(crime) }
    fun editCrime(crime: Crime) = executor.execute { crimeDao.editCrime(crime) }
    fun deleteCrime(crime: Crime) = executor.execute { crimeDao.deleteCrime(crime) }

    fun getPhotoFile(crime: Crime): File = File(filesDir, crime.photoFileName)
}