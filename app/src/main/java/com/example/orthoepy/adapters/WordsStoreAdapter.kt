package com.example.orthoepy.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.orthoepy.R
import com.example.orthoepy.databinding.WordItemBinding
import com.example.orthoepy.wordsmodel.Word

interface WordsStoreActionListener {
    fun wordStoreClick(word: Word)

    fun wordStoreUnClick(word: Word)
}

class WordsStoreAdapter(
    private val actionListener: WordsStoreActionListener
): RecyclerView.Adapter<WordsStoreAdapter.WordsStoreViewHolder>() {

    var wordsStore: List<Word> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsStoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WordItemBinding.inflate(inflater, parent, false)

        return WordsStoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordsStoreViewHolder, position: Int) {
        val wordStore = wordsStore[position]
        with(holder.binding) {
            holder.itemView.tag = wordStore
            wordInList.text = wordStore.wordText
            checkbox.isChecked = wordStore.isChecked
        }

    }

    override fun getItemCount(): Int = wordsStore.size

    inner class WordsStoreViewHolder(
        val binding: WordItemBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View) {
            val wordBinding = WordItemBinding.bind(p0)
            val position = adapterPosition
            val wordStore = wordsStore[position]
            if (position != RecyclerView.NO_POSITION) {
                if (!wordBinding.checkbox.isChecked)
                    actionListener.wordStoreClick(wordStore)
                else
                    actionListener.wordStoreUnClick(wordStore)
                wordStore.isChecked = !wordStore.isChecked
                wordBinding.checkbox.isChecked = wordStore.isChecked
            }
        }
    }
}