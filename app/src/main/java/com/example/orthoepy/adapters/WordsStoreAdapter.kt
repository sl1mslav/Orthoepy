package com.example.orthoepy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orthoepy.entity.Word
import com.example.orthoepy.databinding.WordItemBinding

class WordsStoreAdapter(
    val onClick: (Word) -> Boolean
): ListAdapter<Word, WordsStoreAdapter.WordsStoreViewHolder>(DiffUtilCallback) {

    inner class WordsStoreViewHolder(val binding: WordItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsStoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WordItemBinding.inflate(inflater, parent, false)

        return WordsStoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordsStoreViewHolder, position: Int) {
        val wordStore = getItem(position)
        with(holder.binding) {
            wordInList.text = wordStore.wordText
            checkbox.isChecked = wordStore.isChecked.toBoolean()
            root.setOnClickListener {
                wordStore?.let {
                    if (onClick(it))
                        checkbox.isChecked = !checkbox.isChecked
                }
            }
            checkbox.setOnClickListener {
                wordStore?.let {
                    if (!onClick(it)) {
                        checkbox.isChecked = false
                        checkbox.jumpDrawablesToCurrentState()
                    }
                }
            }
        }
    }
}
object DiffUtilCallback : DiffUtil.ItemCallback<Word>() {
    override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean = oldItem == newItem
    override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean =
        oldItem.id == newItem.id
}

