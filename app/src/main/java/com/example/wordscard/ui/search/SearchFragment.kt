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

class SearchFragment: Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding

    private var word: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        // the word from word list
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

                buttonOn()

                Log.i("query",query)

                return false
            }

        })

        viewModel.state.observe(viewLifecycleOwner, {
            if (viewModel.state.value != null) buttonOff()
        })

        viewModel.word.observe(viewLifecycleOwner,{
            Log.i("word change",viewModel.word.value.toString())})

        binding.textButton.setOnClickListener {
            viewModel.playRaudio()
        }

        binding.collectButton.setOnClickListener {
            if(viewModel.word.value != null)
            viewModel.addWord(viewModel.word.value!!.word+"   ["+viewModel.word.value!!.phonetics[0].text+"] ")
        }

    }

    private fun buttonOn(){
        binding.textButton.isEnabled = true
        binding.collectButton.isEnabled = true
        binding.collectButton.setText(R.string.collect_button)
    }

    private fun buttonOff(){
        binding.textButton.isEnabled = false
        binding.collectButton.isEnabled = false
        binding.collectButton.text = ""
    }

    private fun getWordFromList(word: String?){
        if(word != null)  {
            Log.i("word",word)
            viewModel.getWord(word)
            buttonOn()
        }

    }
}