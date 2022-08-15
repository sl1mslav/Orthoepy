package com.example.orthoepy

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.orthoepy.databinding.ActivityMainBinding
import com.example.orthoepy.entity.UserInterfaceUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.apply {
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item, navController)
                navController.popBackStack(destinationId = item.itemId, inclusive = false)
            }
        }
    }

    // FIXME: find a way to
    //  not make the window pop up on a new subsequent click
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard()
        return super.dispatchTouchEvent(ev)
    }
}