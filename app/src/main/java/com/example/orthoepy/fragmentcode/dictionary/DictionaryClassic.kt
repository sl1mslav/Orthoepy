package com.example.orthoepy.fragmentcode.dictionary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orthoepy.BaseFragment
import com.example.orthoepy.R
import com.example.orthoepy.adapters.WordsDictionaryAdapter
import com.example.orthoepy.databinding.FragmentDictionaryClassicBinding
import com.example.orthoepy.entity.Word
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DictionaryClassic : BaseFragment() {

    private var _binding: FragmentDictionaryClassicBinding? = null
    private val binding get() = _binding!!

    val dictionaryAdapter = WordsDictionaryAdapter { viewModel.markWord(it) }

    private val viewModel: DictionaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDictionaryClassicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.dictionaryRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.dictionaryRecycler.adapter = dictionaryAdapter

        initCollectors()
    }

    private fun initCollectors() {
        launchFlow {
            viewModel.boughtWords.collect {
                dictionaryAdapter.submitList(it)
            }
        }
    }

    companion object {
        var POSITION_ARG = "position_arg"

        @JvmStatic
        fun newInstance(position: Int) = DictionaryClassic().apply {
            arguments = Bundle().apply {
                putInt(POSITION_ARG, position)
            }
        }
    }
}