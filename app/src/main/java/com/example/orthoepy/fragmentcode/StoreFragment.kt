package com.example.orthoepy.fragmentcode

import android.animation.LayoutTransition
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orthoepy.App
import com.example.orthoepy.MainViewModel
import com.example.orthoepy.R
import com.example.orthoepy.adapters.WordsStoreActionListener
import com.example.orthoepy.adapters.WordsStoreAdapter
import com.example.orthoepy.databinding.FragmentStoreBinding
import com.example.orthoepy.wordsmodel.Word
import com.example.orthoepy.wordsmodel.WordsStoreListener
import com.example.orthoepy.wordsmodel.WordsStoreService

class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val wordsStoreService: WordsStoreService
        get() = (requireActivity().application.applicationContext as App).wordsStoreService
    private lateinit var adapter: WordsStoreAdapter
    private val wordsToBuy: MutableList<Word> = mutableListOf()
    private var availableLetters = 0
    private var letterCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        // Animation fix
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        // Viewmodel setup
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // UI Setup
        setUpUI()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        wordsStoreService.removeListener(wordsStoreListener)
        _binding = null
    }

    private fun setUpUI() {

        fun changeCurrencyCounter(text: String) {
            binding.availableLetters.currencyCv.findViewById<TextView>(R.id.available_letters_counter).text =
                getString(R.string.available_letters, text)
        }

        adapter = WordsStoreAdapter(object: WordsStoreActionListener{
            override fun wordStoreClick(word: Word) {
                wordsToBuy.add(word)
                if (letterCounter == 0)
                    binding.addButton.visibility = View.VISIBLE
                letterCounter += word.wordText.length
                changeCurrencyCounter("$availableLetters - $letterCounter")
            }

            override fun wordStoreUnClick(word: Word) {
                letterCounter -= word.wordText.length
                changeCurrencyCounter("$availableLetters - $letterCounter")
                if (letterCounter == 0) {
                    changeCurrencyCounter("$availableLetters")
                    binding.addButton.visibility = View.GONE
                }
                wordsToBuy.remove(word)
            }
        })
        availableLetters = viewModel.getLetterCount()
        changeCurrencyCounter(availableLetters.toString())
        binding.storeRecycler.layoutManager = LinearLayoutManager(activity)
        binding.storeRecycler.adapter = adapter

        wordsStoreService.addListener(wordsStoreListener)

        binding.addButton.visibility = View.GONE

        binding.addButton.setOnClickListener {
            if (letterCounter <= availableLetters) {
                availableLetters -= letterCounter
                viewModel.setLetterCount(availableLetters)
                wordsToBuy.forEach {
                    wordsStoreService.buyWord(it)
                }
                wordsToBuy.clear()
                letterCounter = 0
                it.visibility = View.GONE
                changeCurrencyCounter(availableLetters.toString())
            }
            else {
                Toast.makeText(context, "Недостаточно букв", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val wordsStoreListener: WordsStoreListener = {
        adapter.wordsStore = it
    }
}