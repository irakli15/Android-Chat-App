package com.example.httpchatclient.chatpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.httpchatclient.R
import kotlinx.android.synthetic.main.chat_toolbar_and_recyclerview.view.*

class ChatPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_page, container, false)
        var viewManager = LinearLayoutManager(context)
        viewManager.reverseLayout = true
        var viewAdapter = ChatRecyclerView()

        var recyclerView: RecyclerView = view.chatRecyclerView
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        (activity as AppCompatActivity).setSupportActionBar(view.chat_toolbar)
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        view.chat_toolbar.setNavigationOnClickListener{
            (activity as AppCompatActivity).onBackPressed()
        }
        return view
    }
}