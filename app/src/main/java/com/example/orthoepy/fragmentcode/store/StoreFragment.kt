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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orthoepy.R
import com.example.orthoepy.adapters.WordsStoreAdapter
import com.example.orthoepy.databinding.FragmentStoreBinding
import com.example.orthoepy.entity.UserInterfaceUtils.launchFlow
import com.example.orthoepy.entity.Word
import dagger.hilt.android.AndroidEntryPoint

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
        initRecycler()

        binding.addButton.setOnClickListener {
            viewModel.buyCheckedWords()
        }

        binding.storeSearchOrtho.searchBar.apply {
            addTextChangedListener { viewModel.selectWordsByQuery(it.toString()) }
            setOnClickListener {
                it.requestFocus()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearCheckedWords()
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

    private fun animateButton(toDisappear: Boolean) {
        if (toDisappear && binding.addButton.visibility == View.VISIBLE) {
            binding.addButton.animate().apply {
                duration = 300L
                alpha(0f)
                withEndAction {
                    binding.addButton.visibility = View.GONE
                }
            }.start()
        } else if (!toDisappear && binding.addButton.visibility != View.VISIBLE) {
            with (binding.addButton) {
                visibility = View.VISIBLE
                alpha = 0f
                animate().apply {
                    duration = 300L
                    alpha(1f)
                }.start()
            }
        }
    }

    private fun initRecycler() {
        binding.storeRecycler.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = wordAdapter
        }
        launchFlow {
            viewModel.notBoughtWords.collect {
                wordAdapter.submitList(it)
            }
        }
        launchFlow {
            // TODO: separate stateflows query logic is unnecessary. Look into removing it.
            viewModel.notBoughtWordsByQuery.collect {
                if (!binding.storeSearchOrtho.searchBar.text.isNullOrBlank()) {
                    wordAdapter.submitList(it)
                } else {
                    wordAdapter.submitList(viewModel.notBoughtWords.value)
                }
            }
        }
        launchFlow {
            viewModel.dataPackage.collect {
                val collectedWords = it.first
                val collectedCurrency = it.second?.currencyAmount
                if (collectedWords.isEmpty()) {
                    changeCurrencyCounter("$collectedCurrency")
                } else {
                    changeCurrencyCounter("$collectedCurrency - ${collectedWords.sumOf { it.wordText.length }}")
                }
                animateButton(collectedWords.isEmpty())
            }
        }
    }
}