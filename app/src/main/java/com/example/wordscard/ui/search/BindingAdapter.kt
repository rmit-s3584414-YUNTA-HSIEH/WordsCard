package com.example.wordscard.ui.search

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscard.data.WordDetail

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<WordDetail>?){
    val adapter = recyclerView.adapter as WordDetailAdapter
    adapter.submitList(data)
}