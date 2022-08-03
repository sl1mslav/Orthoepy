package com.example.orthoepy.fragmentcode.dictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.orthoepy.R
import com.example.orthoepy.adapters.DictionaryVPAdapter
import com.example.orthoepy.databinding.FragmentDictionaryBinding
import com.example.orthoepy.databinding.FragmentStoreBinding
import com.google.android.material.tabs.TabLayoutMediator


class DictionaryFragment : Fragment() {

    // TODO: Attach a currency counter
    // TODO: Expandable CardViews with the definitions of words (if possible)

    private var _binding: FragmentDictionaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dictionaryVp = binding.dictionaryVp
        val adapter = DictionaryVPAdapter(this)
        dictionaryVp.adapter = adapter

        TabLayoutMediator(binding.dictionaryTabs, dictionaryVp) {
            tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}