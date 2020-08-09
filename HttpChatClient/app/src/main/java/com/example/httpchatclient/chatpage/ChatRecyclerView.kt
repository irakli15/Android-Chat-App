package com.example.httpchatclient.chatpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.httpchatclient.R
import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import kotlinx.android.synthetic.main.chat_received.view.*
import kotlinx.android.synthetic.main.chat_sent.view.*

class ChatRecyclerView(private val currentUser: User, private val messageThread: MessageThread) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SentMessage(private var view: View) : RecyclerView.ViewHolder(view) {
        fun fillValues(message: Message) {
            view.sentChatText.text = message.messageText
            view.sentChatTime.text = message.sendTime.toString()
        }
    }

    class ReceivedMessage(private var view: View) : RecyclerView.ViewHolder(view) {
        fun fillValues(message: Message) {
            view.receivedChatText.text = message.messageText
            view.receivedChatTime.text = message.sendTime.toString()
        }
    }

    private val entries: MutableList<Message> = mutableListOf()
    private val typeValues = MessageType.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (typeValues[viewType]) {
            MessageType.RECEIVER ->
                ReceivedMessage(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.chat_received,
                        parent,
                        false
                    )
                )
            MessageType.SENDER ->
                SentMessage(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.chat_sent,
                        parent,
                        false
                    )
                )
        }
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SentMessage) {
            holder.fillValues(entries[position])
        } else if (holder is ReceivedMessage) {
            holder.fillValues(entries[position])
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (entries[position].senderId == currentUser.id) MessageType.SENDER.ordinal else MessageType.RECEIVER.ordinal


    fun setData(chatEntries: MutableList<Message>) {
        entries.clear()
        entries.addAll(chatEntries)
        notifyDataSetChanged()
    }

    enum class MessageType {
        SENDER,
        RECEIVER
    }
}
