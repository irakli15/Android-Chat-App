package com.example.httpchatclient.chathistorypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.httpchatclient.R
import kotlinx.android.synthetic.main.fragment_chat_history_page.view.*

class ChatHistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_history_page, container, false)
        var viewManager = LinearLayoutManager(context)
        var viewAdapter = ChatHistoryRecyclerView(findNavController())

        var recyclerView: RecyclerView = view.chatHistoryRecyclerView

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Inflate the layout for this fragment
        return view
    }
}