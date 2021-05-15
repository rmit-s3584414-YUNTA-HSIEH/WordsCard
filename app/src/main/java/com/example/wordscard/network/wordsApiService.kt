package com.example.wordscard.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL =
    "https://api.dictionaryapi.dev/api/v2/entries/en_US/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface WordsApiService{

    @GET("{word}")
    suspend fun getWord(@Path("word") word: String): String
}


object wordsApi{
    val retrofitService: WordsApiService by lazy {
        retrofit.create(WordsApiService::class.java)
    }
}