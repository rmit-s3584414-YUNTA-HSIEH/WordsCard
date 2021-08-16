package com.example.wordscard.ui.review

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordscard.DAO.ReviewDao
import com.example.wordscard.DAO.ReviewDatabase
import com.example.wordscard.ReviewApplication
import com.example.wordscard.data.Review
import com.example.wordscard.data.WordDefinition
import com.example.wordscard.network.wordsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

val learningCurve = listOf(1,2,5,7,21,30)

class ReviewViewModel: ViewModel() {


    var database: ReviewDao = ReviewDatabase.getDatabase(ReviewApplication.applicationContext()).reviewDao()

    private val _word = MutableLiveData<WordDefinition>()
    val word: LiveData<WordDefinition> = _word

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    private val _meaning = MutableLiveData<String>()
    val meaning: LiveData<String> = _meaning

    // use for clicking text and finishing review text
    private val _reuseText = MutableLiveData<String>()
    val reuseText: LiveData<String> = _reuseText

    private var _allReviewWords = MutableLiveData<List<Review>>()
    var allReviewWords: LiveData<List<Review>> = _allReviewWords




    private fun getWord(Query: String): Job {
        return viewModelScope.launch {
            try {
                _word.value = withContext(Dispatchers.IO){wordsApi.retrofitService.getWord(Query)[0]}
            } catch (e: Exception) {
                Log.i("exception", e.message.toString())
            }
        }
    }

    fun playRaudio(){
        try {
            val url: String = _word.value!!.phonetics[0].audio!!
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
            Log.i("exception",e.message.toString())
        }
    }

    private fun displayDetail(){
        var detail = ""
        try {
            for(i in _word.value!!.meanings.indices){
                //add part of speech
                detail = detail + "["+ word.value!!.meanings[i].partOfSpeech+ "] \n\n"
                for(j in _word.value!!.meanings[i].definitions.indices){
                    detail = detail + word.value!!.meanings[i].definitions[j].definition+"\n"
                }
                detail += "\n\n"
            }
            _meaning.value = detail
        }catch (e:Exception){
            Log.i("displayDetail", e.message.toString())
        }
    }

    private fun displayWord(){
        try{
            _text.value = _word.value!!.word +"  [" +_word.value!!.phonetics[0].text +"]"
        }catch (e:Exception){
            Log.i("displayWord", e.message.toString())
        }

    }

    private fun finishReview(){
        _word.value = null
        _meaning.value = null
        _text.value = null

        _reuseText.value = "Congrats!! \n you've done the review today"

    }

    fun setClick(click: Boolean){
        if(!_allReviewWords.value.isNullOrEmpty()){
            if(click){
                _reuseText.value = null
                displayDetail()
            }
            else {
                _reuseText.value = "click for details"
                _meaning.value = null
            }
        }


    }

    fun initial(){
        try {
            viewModelScope.launch {

                //get all review words that need to be reviewed
                _allReviewWords.value = database.findReviewByDate(System.currentTimeMillis())

                //check if whether any word in the list
                if(_allReviewWords.value.isNullOrEmpty()) finishReview()
                else{
                    Log.i("all reviews",_allReviewWords.value.toString())

                    val job: Job = getWord(_allReviewWords.value!![0].word)

                    job.join()
                    displayWord()
                    setClick(false)
                    playRaudio()
                }
            }

        }catch (e: Exception){
            Log.i("review_word",e.message.toString())
        }
    }


    fun switchWord(check: Boolean){

        /*
        if user remember the word:
        1. update the learning carve and date
        2.remove the word in all review words list
        3. retrieve next word randomly
        */

        if(check){
            val arr = _allReviewWords.value!!.toMutableList()
            for(i in arr.indices){
                if(arr[i].word == _word.value!!.word) {
                    Log.i("before updating word",arr[i].toString())
                    //update curve
                    arr[i].curve = if(arr[i].curve != learningCurve.last()) learningCurve[learningCurve.indexOf(arr[i].curve)+1] else 1

                    val date = Calendar.getInstance()
                    date.timeInMillis = System.currentTimeMillis()
                    //add days in calendar
                    date.add(Calendar.DAY_OF_YEAR, arr[i].curve)
                    //update date
                    arr[i].date = date.timeInMillis

                    Log.i("after updating word",arr[i].toString())

                    viewModelScope.launch {

                        database.updateReview(arr[i])
                        arr.removeAt(i)
                        _allReviewWords.value = arr
                        //checking if there are more words to review
                        if(_allReviewWords.value!!.isEmpty()){
                            finishReview()
                        }else{
                            val job = getWord(arr.random().word)
                            job.join()
                            displayWord()
                            setClick(false)
                            playRaudio()
                        }
                    }

                }

            }
        }else{
            if(_allReviewWords.value!!.isEmpty()) {
                finishReview()
            }else{
                //forget the word -> keep the word in the list and review again until remember it
                var randomWord = _allReviewWords.value!!.random().word
                while(randomWord == _word.value!!.word && _allReviewWords.value!!.size>1) randomWord = _allReviewWords.value!!.random().word

                viewModelScope.launch {
                    val job = getWord(randomWord)
                    job.join()
                    displayWord()
                    setClick(false)
                    playRaudio()
                }
            }
        }
    }

}