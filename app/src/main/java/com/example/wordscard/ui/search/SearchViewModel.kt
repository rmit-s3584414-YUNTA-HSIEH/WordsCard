package com.example.wordscard.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordscard.data.WordDefinition
import com.example.wordscard.network.wordsApi
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val _word = MutableLiveData<WordDefinition>()
    val word: LiveData<WordDefinition> = _word

    private val _state = MutableLiveData<String>()
    val state: LiveData<String> = _state



    fun getWord(word: String){
        viewModelScope.launch {
            try{
                _word.value = wordsApi.retrofitService.getWord(word).get(0)
                Log.i("word", _word.value!!.word)
            }catch (e: Exception){
                _state.value = "Failure: ${e.message}"
                Log.i("exception",e.message.toString())
            }
        }

    }
}