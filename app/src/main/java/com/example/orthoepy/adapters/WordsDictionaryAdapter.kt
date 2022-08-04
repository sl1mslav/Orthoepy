package com.example.orthoepy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orthoepy.databinding.DictionaryWordItemBinding
import com.example.orthoepy.databinding.WordItemBinding
import com.example.orthoepy.entity.Word
import com.example.orthoepy.entity.WordCardColors


class WordsDictionaryAdapter(
    val onClick: (Word) -> Unit
) :
    ListAdapter<Word, WordsDictionaryAdapter.WordsDictionaryViewHolder>(DiffUtilCallback) {

    inner class WordsDictionaryViewHolder(val binding: DictionaryWordItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsDictionaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DictionaryWordItemBinding.inflate(inflater, parent, false)

        return WordsDictionaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordsDictionaryViewHolder, position: Int) {
        val wordDictionary = getItem(position)
        bind(holder.binding, wordDictionary)
    }

    private fun bind(binding: DictionaryWordItemBinding, word: Word) {
        with(binding){
            wordInList.text = word.wordTextStress
            marker.setCardBackgroundColor(
                ContextCompat.getColor(
                    marker.context,
                    WordCardColors.values()[word.memorizationLevel].color
                )
            )
            setAnimFrame(this, word)
            root.setOnClickListener {
                onClick(word)
                playAnim(this, word.isFavourite.toBooleanStrict())
                word.isFavourite = (!word.isFavourite.toBoolean()).toString()
            }
            animatingButton.setOnClickListener {
                onClick(word)
                playAnim(this, word.isFavourite.toBooleanStrict())
                word.isFavourite = (!word.isFavourite.toBooleanStrict()).toString()
            }
        }

    }

    private fun setAnimFrame(binding: DictionaryWordItemBinding, word: Word) {
        if (word.isFavourite.toBooleanStrict()) {
            binding.animatingButton.frame = ANIM_END_FRAME
            binding.animatingButtonFrame.visibility = View.GONE
        }
        else {
            binding.animatingButton.frame = ANIM_START_FRAME
            binding.animatingButtonFrame.visibility = View.VISIBLE
        }
    }

    private fun playAnim(binding: DictionaryWordItemBinding, isFavourite: Boolean) {
        binding.animatingButton.apply {
            setMinAndMaxFrame(ANIM_START_FRAME, ANIM_END_FRAME)
            if (isFavourite) {
                speed = -LIKE_ANIM_SPEED
                binding.animatingButtonFrame.visibility = View.VISIBLE
            }
            else {
                speed = LIKE_ANIM_SPEED
                binding.animatingButtonFrame.visibility = View.GONE
            }
            playAnimation()
        }
    }

    private companion object DictionaryConstants {
        const val ANIM_START_FRAME = 0
        const val ANIM_END_FRAME = 20
        const val LIKE_ANIM_SPEED = 2f
    }
}