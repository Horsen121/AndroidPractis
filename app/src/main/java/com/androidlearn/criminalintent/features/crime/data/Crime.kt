package com.androidlearn.criminalintent.features.crime.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity
data class Crime(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val date: Date = Date(),
    var isSolved: Boolean = false
)

class CrimeTypeConverters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }
}
