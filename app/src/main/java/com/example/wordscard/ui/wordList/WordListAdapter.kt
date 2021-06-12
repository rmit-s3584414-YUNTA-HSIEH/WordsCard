package com.example.wordscard.ui.wordList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscard.R

class WordListAdapter( val items: List<String>):
    RecyclerView.Adapter<WordListAdapter.ListViewHolder>(){
    class ListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.list_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,parent,false
        ))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.name.text = items[position]
        holder.name.setOnClickListener {
            val arr = holder.name.text.toString().split(" ")
            val bundle = bundleOf(Pair("word",arr[0]))
            it.findNavController().navigate(R.id.navigation_search, bundle)
            Log.i("card click",holder.name.text.toString() ) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}