package com.example.wordscard.network

import com.example.wordscard.data.WordDefinition
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL =
    "https://api.dictionaryapi.dev/api/v2/entries/en_GB/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WordsApiService{

    @GET("{word}")
     suspend fun getWord(@Path("word") word: String): List<WordDefinition>
}


object wordsApi{
    val retrofitService: WordsApiService by lazy {
        retrofit.create(WordsApiService::class.java)
    }
}