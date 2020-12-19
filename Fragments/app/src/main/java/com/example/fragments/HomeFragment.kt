package com.example.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.navigate
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val counter = HomeFragmentArgs.fromBundle(requireArguments()).counter
        textViewHome.text = "$counter"
        btnHome.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeFragmentSelf(counter + 1))
        }
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.home)
            setDisplayHomeAsUpEnabled(true)
        }
    }
}