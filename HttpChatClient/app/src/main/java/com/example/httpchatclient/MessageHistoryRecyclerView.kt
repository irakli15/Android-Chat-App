package com.example.httpchatclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_history_entry.view.*

class MessageHistoryRecyclerView() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MessageHistoryEntry(private var view: View) : RecyclerView.ViewHolder(view) {
        fun fillValues() {
            view.historyUserNameField.text = "user name"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageHistoryEntry(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_history_entry, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MessageHistoryEntry){
            holder.fillValues()
        }
    }
}
