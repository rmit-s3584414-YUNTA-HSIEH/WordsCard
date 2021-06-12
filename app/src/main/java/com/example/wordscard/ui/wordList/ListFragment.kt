package com.example.wordscard.ui.wordList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscard.R

class ListFragment: Fragment() {
    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list,container,false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        val recyclerView: RecyclerView= view.findViewById(R.id.word_list_recyclerview)
        listViewModel.listwords.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = WordListAdapter(listViewModel.listwords.value!!.toList())
        })

    }

}