package com.example.wordscard.ui.search

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordscard.data.WordDefinition
import com.example.wordscard.data.WordDetail
import com.example.wordscard.network.wordsApi
import kotlinx.coroutines.launch


class SearchViewModel: ViewModel() {
    private val _word = MutableLiveData<WordDefinition>()
    val word: LiveData<WordDefinition> = _word


    private val _wordDetail = MutableLiveData<List<WordDetail>>()
    val wordDetail: LiveData<List<WordDetail>> = _wordDetail


    private val _state = MutableLiveData<String>()
    val state: LiveData<String> = _state



    fun getWord(Query: String){
        viewModelScope.launch{
            try{
                _word.value = wordsApi.retrofitService.getWord(Query).get(0)
                Log.i("word", _word.value!!.toString())
                definitionHelper(_word.value)
            }catch (e: Exception){
                _state.value = "Failure: ${e.message}"
                Log.i("exception",e.message.toString())
            }
        }

    }


    fun PlayRaudio(){
        try {
            val url: String = _word.value!!.phonetics[0].audio
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                prepare() // might take long! (for buffering, etc)
                start()
            }
        }catch (e: Exception){
            _state.value = "Failure: ${e.message}"
            Log.i("exception",e.message.toString())
        }
    }
    fun definitionHelper(word: WordDefinition?){
        val numDetail = mutableListOf<WordDetail>()
        try{

            for(i in 0..word!!.meanings.lastIndex){
                val detail= WordDetail("["+ word!!.meanings[i].partOfSpeech+ "]","")
                for(j in 0..word!!.meanings[i].definitions.lastIndex){
                    detail.detail = detail.detail + word!!.meanings[i].definitions[j].definition +"\n\n"
                }

                numDetail.add(detail)
            }
            _wordDetail.value = numDetail.toList()
            Log.i("Detail",_wordDetail.value.toString())

        }catch (e: Exception){
            _state.value = "Failure: ${e.message}"
            Log.i("exception",e.message.toString())
        }

    }

}