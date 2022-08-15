package com.example.orthoepy.fragmentcode.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.orthoepy.R
import com.example.orthoepy.databinding.FragmentHomeBinding
import com.example.orthoepy.entity.UserInterfaceUtils.launchFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.classicTraining.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_trainingFragment)
        }

        launchFlow {
            viewModel.availableLetters.collect {
                changeCurrencyCounter(it?.currencyAmount.toString())
            }
        }
    }

    private fun changeCurrencyCounter(text: String) {
        binding.availableLetters.currencyCv.findViewById<TextView>(R.id.available_letters_counter).text =
            getString(R.string.available_letters, text)
    }
}