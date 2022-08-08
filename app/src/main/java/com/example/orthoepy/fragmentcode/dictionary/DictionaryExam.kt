package com.example.orthoepy.fragmentcode.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orthoepy.adapters.WordsDictionaryAdapter
import com.example.orthoepy.databinding.FragmentDictionaryExamBinding
import com.example.orthoepy.entity.UserInterfaceUtils.launchFlow


class DictionaryExam : Fragment() {

    private var _binding: FragmentDictionaryExamBinding? = null
    private val binding get() = _binding!!

    val dictionaryAdapter = WordsDictionaryAdapter { viewModel.markWord(it) }

    private val viewModel: DictionaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDictionaryExamBinding.inflate(inflater, container, false)
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

        launchFlow {
            viewModel.examWords.collect {
                dictionaryAdapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        var POSITION_ARG = "position_arg"

        @JvmStatic
        fun newInstance(position: Int) = DictionaryExam().apply {
            arguments = Bundle().apply {
                putInt(POSITION_ARG, position)
            }
        }
    }
}