package com.example.wordscard

import android.app.Application
import android.content.Context


class ReviewApplication: Application() {
    init {
        instance = this
    }

    companion object{
        private var instance: ReviewApplication? = null

        fun applicationContext(): Context{
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = ReviewApplication.applicationContext()
    }
}