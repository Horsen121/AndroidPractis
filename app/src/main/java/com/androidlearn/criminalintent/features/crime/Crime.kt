package com.androidlearn.criminalintent.features.crime

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Crime(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val date: Date = Date(),
    var isSolved: Boolean = false
)