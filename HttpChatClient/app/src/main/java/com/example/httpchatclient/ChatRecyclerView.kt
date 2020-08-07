package com.example.httpchatclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_sent.view.*

class ChatRecyclerView() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ChatMessage(private var view: View) : RecyclerView.ViewHolder(view) {
        fun fillValues() {
            view.sentChatText.text = "a message"
            view.sentChatTime.text = "6 pm"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatMessage(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_sent, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ChatMessage){
            holder.fillValues()
        }
    }
}
