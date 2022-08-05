package com.example.orthoepy.fragmentcode.dictionary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.orthoepy.BaseFragment
import com.example.orthoepy.adapters.DictionaryVPAdapter
import com.example.orthoepy.databinding.FragmentDictionaryBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DictionaryFragment : BaseFragment() {

    // TODO: Attach a currency counter
    // TODO: Expandable CardViews with the definitions of words (if possible)

    private var _binding: FragmentDictionaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DictionaryViewModel by activityViewModels()

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

        TabLayoutMediator(binding.dictionaryTabs, dictionaryVp) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

        dictionaryVp.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // TODO: move this to a function
                    when (position) {
                        0 -> {
                            initDictClassicCollectors(dictionaryVp)
                        }
                    }
                }
            }
        )
        binding.storeSearchOrtho.searchBar.addTextChangedListener {
            viewModel.selectWordsByQuery(it.toString())
        }
    }

    private fun initDictClassicCollectors(pager: ViewPager2) {
        val dictionaryClassic =
            pager.getCurrentFragment(childFragmentManager) as DictionaryClassic
        dictionaryClassic.launchFlow {
            Log.d("tag", "launching flow")
            viewModel.boughtWordsByQuery.collect {
                if (!binding.storeSearchOrtho.searchBar.text.isNullOrBlank()) {
                    dictionaryClassic.dictionaryAdapter.submitList(it)
                } else {
                    dictionaryClassic.dictionaryAdapter.submitList(viewModel.boughtWords.value)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}