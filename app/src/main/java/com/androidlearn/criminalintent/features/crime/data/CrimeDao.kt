package com.androidlearn.criminalintent.features.crime.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime")
    fun getCrimes(): Flow<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: Long): Flow<Crime?>

    @Delete
    fun deleteCrime(crime: Crime)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun editCrime(crime: Crime)

    @Insert
    fun insertCrime(crime: Crime)
}