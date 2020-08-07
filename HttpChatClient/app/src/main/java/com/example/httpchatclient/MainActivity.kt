package com.example.httpchatclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_chat_page)
//        setSupportActionBar(chat_toolbar)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setDisplayShowTitleEnabled(false)
//        chat_toolbar.title = "user name"
        initChatRecyclerVew()
//        initChatHistoryRecyclerVew()
    }

    fun initChatHistoryRecyclerVew() {
        var viewManager = LinearLayoutManager(this)
        var viewAdapter = MessageHistoryRecyclerView()

        var recyclerView: RecyclerView = findViewById(R.id.chatHistoryRecyclerView)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun initChatRecyclerVew() {
        var viewManager = LinearLayoutManager(this)
        var viewAdapter = ChatRecyclerView()

        var recyclerView: RecyclerView = findViewById(R.id.chatRecyclerView)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}