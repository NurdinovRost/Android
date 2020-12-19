package com.example.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.material.navigation.onNavigationItemSelected
import com.google.android.material.navigation.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {


        val navGraphIds = listOf(
            R.navigation.home_navigation,
            R.navigation.dictionary_navigation,
            R.navigation.chat_navigation
        )

        val controller = bottom_navigation?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.my_nav_host_fragment,
            intent = intent
        ) ?: nav_view?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.my_nav_host_fragment,
            intent = intent
        )
        currentNavController = controller
        savedInstanceState?.apply {
            val selectedItemId = getInt(BOTTOM_NAVIGATION_SELECTED_ITEM_KEY)
            bottom_navigation?.selectedItemId = selectedItemId
            nav_view?.onNavigationItemSelected(selectedItemId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation(savedInstanceState)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (currentNavController?.value?.navigateUp() == false){
            finish()
            false
        } else {
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val selectedItemId = bottom_navigation?.selectedItemId ?:nav_view?.checkedItem?.itemId ?: 0
        outState.putInt(BOTTOM_NAVIGATION_SELECTED_ITEM_KEY, selectedItemId)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val BOTTOM_NAVIGATION_SELECTED_ITEM_KEY = "bottom_navigation_selected_item"
    }

}
