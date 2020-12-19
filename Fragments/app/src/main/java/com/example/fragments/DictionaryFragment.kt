package com.example.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.navigate
import kotlinx.android.synthetic.main.fragment_dictionary.*
import kotlinx.android.synthetic.main.fragment_home.*

class DictionaryFragment : Fragment(R.layout.fragment_dictionary) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val counter = DictionaryFragmentArgs.fromBundle(requireArguments()).counter
        textViewDictionary.text = "$counter"
        btnDictionary.setOnClickListener {
            navigate(DictionaryFragmentDirections.actionDictionaryFragmentSelf(counter + 1))
        }
    }
}