package com.example.orthoepy.fragmentcode.dictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.orthoepy.R
import com.example.orthoepy.databinding.FragmentDictionaryClassicBinding


class DictionaryClassic : Fragment() {

    // TODO: setup a proper adapter for the fragment

    private var _binding: FragmentDictionaryClassicBinding? = null
    private val binding get() = _binding!!

    var flag = false

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

        // FIXME: implement proper animation within an adapter
        binding.testDictionaryItem.animatingButton.setOnClickListener {
            if (!flag) {
                binding.testDictionaryItem.animatingButton.apply {
                    setMinAndMaxFrame(0, 20)
                    speed = 2f
                    playAnimation()
                    binding.testDictionaryItem.animatingButtonFrame.visibility = View.GONE
                    flag = true
                }
            }
            else {
                binding.testDictionaryItem.animatingButton.apply {
                    setMinAndMaxFrame(0, 20)
                    speed = -2f
                    playAnimation()
                    binding.testDictionaryItem.animatingButtonFrame.visibility = View.VISIBLE
                    flag = false
                }
            }
        }

        binding.testDictionaryItem.marker.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pink))
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