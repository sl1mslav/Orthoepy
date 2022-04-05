package com.example.orthoepy.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.orthoepy.fragmentcode.DictionaryClassic
import com.example.orthoepy.fragmentcode.DictionaryExam
import com.example.orthoepy.fragmentcode.DictionaryPersonal
import com.example.orthoepy.fragmentcode.StoreFragment

class DictionaryVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

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