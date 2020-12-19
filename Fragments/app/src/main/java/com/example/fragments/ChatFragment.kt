package com.example.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.navigate
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_dictionary.*
import kotlinx.android.synthetic.main.fragment_dictionary.textViewDictionary

class ChatFragment : Fragment(R.layout.fragment_chat) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val counter = ChatFragmentArgs.fromBundle(requireArguments()).counter
        textViewChat.text = "$counter"
        btnChat.setOnClickListener {
            navigate(ChatFragmentDirections.actionChatFragmentSelf(counter + 1))
        }
    }
}