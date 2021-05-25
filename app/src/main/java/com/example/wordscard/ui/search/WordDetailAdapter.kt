package com.example.wordscard.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscard.data.WordDetail
import com.example.wordscard.databinding.ViewItemBinding

class WordDetailAdapter: ListAdapter<WordDetail,WordDetailAdapter.WordDetailViewHolder>(DiffCallback) {

    class WordDetailViewHolder(
        private var binding:ViewItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(wordDetail: WordDetail){
            binding.item = wordDetail
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback: DiffUtil.ItemCallback<WordDetail>(){

        override fun areContentsTheSame(oldItem: WordDetail, newItem: WordDetail): Boolean {
            return oldItem.detail == newItem.detail
        }

        override fun areItemsTheSame(oldItem: WordDetail, newItem: WordDetail): Boolean {
            return oldItem.detail == newItem.detail
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int
    ):WordDetailViewHolder{
        return WordDetailViewHolder(
            ViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder:WordDetailAdapter.WordDetailViewHolder, position: Int){
        val wordDetail = getItem(position)
        holder.bind(wordDetail)

    }
}