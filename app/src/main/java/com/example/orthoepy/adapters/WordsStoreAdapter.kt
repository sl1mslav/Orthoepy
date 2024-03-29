package com.example.orthoepy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orthoepy.databinding.WordItemBinding
import com.example.orthoepy.entity.Word

class WordsStoreAdapter(
    val onClick: (Word) -> Boolean
) : ListAdapter<Word, WordsStoreAdapter.WordsStoreViewHolder>(DiffUtilCallback) {

    // TODO: Update the onClick logic akin to DictionaryAdapter;
    //  right now, multiple clickListeners are set to a single holder.

    inner class WordsStoreViewHolder(val binding: WordItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsStoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WordItemBinding.inflate(inflater, parent, false)

        return WordsStoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordsStoreViewHolder, position: Int) {
        val wordStore = getItem(position)
        with(holder.binding) {
            // TODO: look into removing database isChecked check by using id comparisons
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

