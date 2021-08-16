package com.example.wordscard.ui.search

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordscard.DAO.ReviewDatabase
import com.example.wordscard.ReviewApplication
import com.example.wordscard.data.Review
import com.example.wordscard.data.WordDefinition
import com.example.wordscard.data.WordDetail
import com.example.wordscard.network.wordsApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchViewModel: ViewModel() {

    private var database: DatabaseReference = Firebase.database.reference


    private val _word = MutableLiveData<WordDefinition>()
    val word: LiveData<WordDefinition> = _word


    private val _wordDetail = MutableLiveData<List<WordDetail>>()
    val wordDetail: LiveData<List<WordDetail>> = _wordDetail


    private val _state = MutableLiveData<String>()
    val state: LiveData<String> = _state



    fun getWord(Query: String){
        viewModelScope.launch{

            try{
                _state.value=null
                _word.value = withContext(Dispatchers.IO){wordsApi.retrofitService.getWord(Query)[0]}
                Log.i("word", _word.value!!.toString())
                definitionHelper(_word.value)
            }catch (e: Exception){
                _state.value ="no definition of the word"
                _word.value =null
                _wordDetail.value =null
                Log.i("json exception",e.message.toString())
            }
        }
        Log.i("get word fun", "suspend fun running")

    }


    fun playRaudio(){
        try {
            val url: String = "https:"+ _word.value!!.phonetics[0].audio!!
            Log.i("audio", url)
            MediaPlayer().apply {
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
            Log.i("audio exception",e.message.toString())
        }
    }

    //help to display accurate details of word in view
    private fun definitionHelper(word: WordDefinition?){
        val numDetail = mutableListOf<WordDetail>()
        try{

            for(i in 0..word!!.meanings.lastIndex){
                val detail= WordDetail("["+ word.meanings[i].partOfSpeech+ "]","")
                for(j in 0..word.meanings[i].definitions.lastIndex){
                    detail.detail = detail.detail + word.meanings[i].definitions[j].definition +"\n\n"
                }
                numDetail.add(detail)
            }
            _wordDetail.postValue(numDetail.toList())
            Log.i("Detail",_wordDetail.value.toString())

        }catch (e: Exception){
            Log.i("helper exception",e.message.toString())
        }

    }

    // insert word in room database
    private fun insertWord(str: String){
        viewModelScope.launch{
            val database = ReviewDatabase.getDatabase(ReviewApplication.applicationContext()).reviewDao()
            val reviewWord = Review(null,str,System.currentTimeMillis(),1)
            database.insertAll(reviewWord)
        }
    }
    //add word in firebase database
    fun addWord(w: String){
        database.child("words").get().addOnSuccessListener {
            var existed = false
            var value: String
            for(i in it.children){
                value = i.value.toString()
                if(w == value) existed = true
            }
            if(!existed) {
                database.child("words").push().setValue(w)
                insertWord(word.value!!.word)
            }

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

}