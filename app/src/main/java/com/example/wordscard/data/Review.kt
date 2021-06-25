package com.example.wordscard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val word: String,
    var date: Long,
    var curve: Int
        )