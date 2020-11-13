package com.example.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView

private var currentNavController: LiveData<NavController>? = null

private fun setupBottomNavigation() {
    val bottomNavigationView = findViewByID<BottomNavigationView>(R.id.bottom_navigation)
    val navGraphIds = listOf(
        R.navigation.home_navigation,
        R.navigation.dictionary_navigation,
        R.navigation.chat_navigation
    )

    val controller = bottomNavigationView.setupWithNavController(
        navGraphIds = navGraphIds,
        fragmentManager = supportFragmentManager,
        containerId = R.id.my_nav_host_fragment,
        intent = intent
    )
    currentNavController = controller
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    setupBottomNavigation()
}

override fun onSupportNavigateUp(): Bollean {
    return currentNavController?.value?.navigateUp() ?: false
}