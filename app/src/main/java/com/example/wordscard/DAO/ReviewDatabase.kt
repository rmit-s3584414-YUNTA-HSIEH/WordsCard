package com.example.wordscard.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wordscard.data.Review


@Database(entities = arrayOf(Review::class),version = 1)
abstract class ReviewDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao

    //create singleton object
    companion object{
       // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: ReviewDatabase? = null

        fun getDatabase(context: Context): ReviewDatabase{
            //if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReviewDatabase::class.java,
                    "review"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}