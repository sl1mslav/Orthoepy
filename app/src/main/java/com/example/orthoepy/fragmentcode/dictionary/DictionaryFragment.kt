package com.example.orthoepy.fragmentcode.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.orthoepy.R
import com.example.orthoepy.adapters.DictionaryVPAdapter
import com.example.orthoepy.adapters.WordsDictionaryAdapter
import com.example.orthoepy.databinding.FragmentDictionaryBinding
import com.example.orthoepy.entity.DictionaryFragmentPage
import com.example.orthoepy.entity.UserInterfaceUtils.getCurrentFragment
import com.example.orthoepy.entity.UserInterfaceUtils.launchFlow
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DictionaryFragment : Fragment() {

    // TODO: Expandable CardViews with the definitions of words (if possible)
    // TODO: Apply placeholder text on all fragments with empty RVs

    private var _binding: FragmentDictionaryBinding? = null
    private val binding get() = _binding!!

    private var page: DictionaryFragmentPage = DictionaryFragmentPage.Classic

    private var currentRvAdapter: WordsDictionaryAdapter? = null

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
        dictionaryVp.apply {
            this.adapter = adapter
            offscreenPageLimit = 2
        }

        TabLayoutMediator(binding.dictionaryTabs, dictionaryVp) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

        setUpViewPagerCallback(dictionaryVp)

        binding.storeSearchOrtho.searchBar.apply {
            addTextChangedListener { viewModel.selectWordsByQuery(it.toString(), page) }
            setOnClickListener {
                it.requestFocus()
            }
        }

        initFragmentCollectors()
    }

    private fun setUpViewPagerCallback(pager: ViewPager2) {
        pager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    page = when (position) {
                        0 -> DictionaryFragmentPage.Classic
                        1 -> DictionaryFragmentPage.Exam
                        2 -> DictionaryFragmentPage.Personal
                        else -> DictionaryFragmentPage.Classic
                    }
                    currentRvAdapter = when (position) {
                        0 -> (pager.getCurrentFragment(childFragmentManager) as DictionaryClassic).dictionaryAdapter
                        1 -> (pager.getCurrentFragment(childFragmentManager) as DictionaryExam).dictionaryAdapter
                        2 -> (pager.getCurrentFragment(childFragmentManager) as DictionaryPersonal).dictionaryAdapter
                        else -> null
                    }
                }
            }
        )
    }

    private fun initFragmentCollectors() {
        launchFlow {
            viewModel.wordsByQuery.collect {
                currentRvAdapter?.submitList(it)
            }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}