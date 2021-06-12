package com.example.wordscard.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wordscard.R
import com.example.wordscard.databinding.FragmentSearchBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchFragment: Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding

    private lateinit var database: DatabaseReference

    private var word: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        word = arguments?.getString("word")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchViewModel = viewModel
        //Fragment與Fragment中的View的生命週期並不一致，
        // 需要讓observer感知Fragment中的View的生命週期而非Fragment，
        // 因此Android專門構造了Fragment中的View的LifecycleOwner，即viewLifecycleOwner
        binding.lifecycleOwner = viewLifecycleOwner
        database = Firebase.database.reference
        binding.recyclerView.adapter = WordDetailAdapter()
        binding.searchBar.queryHint = "type the word here"

        //show word definition from list fragment
        getWordFromList(word)

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                viewModel.getWord(query)

                if(viewModel.word != null) buttonOn()

                Log.i("query",query)

                return false
            }

        })

        binding.textButton.setOnClickListener {
            viewModel.PlayRaudio()
        }

        binding.collectButton.setOnClickListener {
            if(viewModel.word.value != null)
            addTheWord(viewModel.word.value!!.word+"   ["+viewModel.word.value!!.phonetics[0].text+"] ")
        }
    }

    private fun buttonOn(){
        binding.textButton.isEnabled = true
        binding.collectButton.isEnabled = true
        binding.collectButton.setText(R.string.collect_button)
    }

    private fun addTheWord(w: String){
        var data = database.child("words").get().addOnSuccessListener {
            var existed = false
            var value = ""
            for(i in it.children){
                value = i.value.toString()
                if(w == value) existed = true
            }
            if(!existed) database.child("words").push().setValue(w)

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun getWordFromList(word: String?){
        if(word != null)  {
            viewModel.getWord(word)
            buttonOn()
        }

    }
}