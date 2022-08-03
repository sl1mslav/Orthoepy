package com.example.orthoepy.fragmentcode.store

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orthoepy.R
import com.example.orthoepy.adapters.WordsStoreAdapter
import com.example.orthoepy.entity.Word
import com.example.orthoepy.databinding.FragmentStoreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    private val wordAdapter = WordsStoreAdapter { onItemClick(it) }

    private val viewModel: StoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        // Animation fix
        binding.availableLetters.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.storeRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.storeRecycler.adapter = wordAdapter
        initRecyclerUpdates()

        binding.addButton.setOnClickListener {
            viewModel.buyCheckedWords()
        }

        binding.storeSearchOrtho.searchBar.addTextChangedListener {
            viewModel.selectWordsByQuery(it.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // This function returns false only in the case of insufficient funds
    private fun onItemClick(item: Word): Boolean {
        return if (viewModel.dataPackage.value.first.contains(item)) {
            viewModel.removeWordToBuy(item)
            item.isChecked = "false"
            true
        } else if (!viewModel.addWordToBuy(item)) {
            Toast.makeText(requireContext(), R.string.insufficient_funds, Toast.LENGTH_SHORT).show()
            false
        } else {
            item.isChecked = "true"
            true
        }
    }

    private fun changeCurrencyCounter(text: String) {
        binding.availableLetters.currencyCv.findViewById<TextView>(R.id.available_letters_counter).text =
            getString(R.string.available_letters, text)
    }

    private fun initRecyclerUpdates() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this.launch {
                    viewModel.notBoughtWords.collect {
                        wordAdapter.submitList(it)
                    }
                }
                this.launch {
                    viewModel.notBoughtWordsByQuery.collect {
                        if (!binding.storeSearchOrtho.searchBar.text.isNullOrBlank()) {
                            wordAdapter.submitList(it)
                        }
                        else {
                            wordAdapter.submitList(viewModel.notBoughtWords.value)
                        }
                    }
                }
                this.launch {
                    viewModel.dataPackage.collect {
                        val collectedWords = it.first
                        val collectedCurrency = it.second
                        if (collectedWords.isEmpty()) {
                            binding.addButton.alpha = 1f
                            binding.addButton.animate().apply {
                                duration = 300L
                                alpha(0f)
                                withEndAction {
                                    binding.addButton.visibility = View.GONE
                                }
                            }.start()
                            changeCurrencyCounter("$collectedCurrency")
                        } else {
                            if (binding.addButton.visibility != View.VISIBLE) {
                                binding.addButton.visibility = View.VISIBLE
                                binding.addButton.alpha = 0f
                                binding.addButton.animate().apply {
                                    duration = 300L
                                    alpha(1f)
                                }.start()
                            }
                            changeCurrencyCounter("$collectedCurrency - ${collectedWords.sumOf { it.wordText.length }}")
                        }
                    }
                }
            }
        }
    }
}