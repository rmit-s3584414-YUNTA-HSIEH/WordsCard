package com.example.wordscard.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordscard.network.wordsApi
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val _word = MutableLiveData<String>()
    val word: LiveData<String> = _word



    fun getWord(word: String){
        viewModelScope.launch {
            _word.value = wordsApi.retrofitService.getWord(word)
            Log.i("word",_word.value.toString())
        }

    }
}