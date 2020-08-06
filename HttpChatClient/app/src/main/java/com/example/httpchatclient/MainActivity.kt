package com.example.httpchatclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_history_page)
        var viewManager = LinearLayoutManager(this)
        var viewAdapter = MessageHistoryRecyclerView()

        var recyclerView : RecyclerView = findViewById(R.id.chatHistoryRecyclerView)
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}