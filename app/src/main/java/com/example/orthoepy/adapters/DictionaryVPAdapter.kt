package com.example.orthoepy.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.orthoepy.fragmentcode.dictionary.DictionaryClassic
import com.example.orthoepy.fragmentcode.dictionary.DictionaryExam
import com.example.orthoepy.fragmentcode.dictionary.DictionaryPersonal

class DictionaryVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // TODO: Consider using a ViewPager with the Navigation Component if possible
    private val TAB_TITLES = arrayOf(
        "Классика",
        "Экзамен",
        "Любимое"
    )

    fun getTitle(position: Int) = TAB_TITLES[position]

    override fun getItemCount(): Int = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DictionaryClassic.newInstance(position)
            1 -> DictionaryExam.newInstance(position)
            else -> DictionaryPersonal.newInstance(position)
        }
    }
}