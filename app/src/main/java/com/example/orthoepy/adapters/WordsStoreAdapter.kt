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
    // Maybe add some more functions here
    fun wordStoreClick(word: Word)

    fun wordStoreUnClick(word: Word)
}

class WordsStoreAdapter(
    private val actionListener: WordsStoreActionListener
): RecyclerView.Adapter<WordsStoreAdapter.WordsStoreViewHolder>(), View.OnClickListener {

    var wordsStore: List<Word> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val wordStore: Word = v.tag as Word
        val binding = WordItemBinding.bind(v)
        //there is a bug - 2 checkboxes in list check at the same time
        binding.checkbox.isChecked = !binding.checkbox.isChecked
        if (binding.checkbox.isChecked)
            actionListener.wordStoreClick(wordStore)
        else
            actionListener.wordStoreUnClick(wordStore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsStoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WordItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return WordsStoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordsStoreViewHolder, position: Int) {
        val wordStore = wordsStore[position]
        with(holder.binding) {
            holder.itemView.tag = wordStore
            wordInList.text = wordStore.wordText
        }
    }

    override fun getItemCount(): Int = wordsStore.size

    class WordsStoreViewHolder(
        val binding: WordItemBinding
    ): RecyclerView.ViewHolder(binding.root)
}