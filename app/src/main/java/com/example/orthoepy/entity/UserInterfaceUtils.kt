package com.example.orthoepy.entity

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.helper.widget.Flow
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.orthoepy.R
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

object UserInterfaceUtils {

    fun Fragment.hideKeyboard() {
        view?.let {
            activity?.hideKeyboard()
        }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        if (view is AppCompatEditText) {
            view.clearFocus()
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun Fragment.launchFlow(action: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                action()
            }
        }
    }

    fun ViewPager2.getCurrentFragment(fragmentManager: FragmentManager): Fragment? {
        return fragmentManager.findFragmentByTag("f$currentItem")
    }

    fun Flow.addChip(context: Context, text: String, isCorrectLetter: Boolean) {
        val parentView = parent as ViewGroup
        val view = LayoutInflater.from(context).inflate(R.layout.chip_letter_item, parentView, false)
        view.id = View.generateViewId()
        (view as Chip).text = text
        parentView.addView(view)
        addView(view)
        if (isCorrectLetter) {
            view.setOnClickListener {
                Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            view.setOnClickListener {
                Toast.makeText(context, "Incorrect!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}