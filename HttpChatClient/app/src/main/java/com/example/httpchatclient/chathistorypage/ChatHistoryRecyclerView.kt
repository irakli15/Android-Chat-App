package com.example.httpchatclient.chathistorypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.httpchatclient.R
import kotlinx.android.synthetic.main.chat_history_entry.view.*

class ChatHistoryRecyclerView(val navController: NavController) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var clickListener  = View.OnClickListener {
        navController.navigate(R.id.action_chatHistoryFragment_to_chatPageFragment)
    }

    inner class ChatHistoryEntry(private var view: View) : RecyclerView.ViewHolder(view) {
        fun fillValues() {
            view.historyUserNameField.text = "user name"
            view.setOnClickListener (clickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatHistoryEntry(
            LayoutInflater.from(parent.context).inflate(
                R.layout.chat_history_entry,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ChatHistoryEntry){
            holder.fillValues()
        }
    }
}
