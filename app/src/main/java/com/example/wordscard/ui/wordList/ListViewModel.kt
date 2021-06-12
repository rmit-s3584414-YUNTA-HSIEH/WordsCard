package com.example.wordscard.ui.wordList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListViewModel : ViewModel(){

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    private val _listWords = MutableLiveData<List<String>>()
    val listwords: LiveData<List<String>> = _listWords

    private var database : DatabaseReference = Firebase.database.reference

    init {
        listenListWords()
    }
    private fun listenListWords() {
        val li = mutableListOf<String>()
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!= null && snapshot!!.hasChild("words")) {
                    for (i in snapshot!!.child("words").children) {
                        li.add(i.value.toString())
                    }
                    _listWords.value = li
                }
                Log.i("list words", _listWords.value.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i("get list","get list cancelled")
            }

        })
    }

}