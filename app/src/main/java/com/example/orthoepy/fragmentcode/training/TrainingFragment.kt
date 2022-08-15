package com.example.orthoepy.fragmentcode.training

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Flow
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.orthoepy.databinding.TrainingFragmentBinding
import com.example.orthoepy.entity.UserInterfaceUtils.addChip
import com.example.orthoepy.entity.UserInterfaceUtils.launchFlow
import com.example.orthoepy.entity.Word
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainingFragment : Fragment() {

    private var _binding: TrainingFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrainingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrainingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchFlow {
            viewModel.getRandomAvailableWords().collect {
                // TODO: almost done! time to update the logic/layout/animations for multiple words...
                //  also memorizationLevel logic
                createGroup(it.first(), binding.chipFlow)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("sus", "destroyed")
    }


    private fun createGroup(word: Word, flow: Flow) {
        word.wordText.uppercase().forEachIndexed { index, letter ->
            flow.addChip(requireContext(), letter.toString(), index == word.stressIndex)
        }
    }
}