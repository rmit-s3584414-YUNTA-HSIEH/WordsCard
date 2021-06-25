package com.example.wordscard.DAO

import androidx.room.*
import com.example.wordscard.data.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review")
    suspend fun getAll(): List<Review>

    @Delete
    suspend fun delete(review: Review)

    @Update
    suspend fun updateReview(vararg review: Review)

    @Insert
    suspend fun insertAll(vararg reviews: Review)

    @Query("SELECT * FROM review WHERE word LIKE :str")
    suspend fun findReviewByWord(str: String): Review

    @Query("SELECT * FROM review WHERE date < :time")
    suspend fun findReviewByDate(time: Long): List<Review>
}