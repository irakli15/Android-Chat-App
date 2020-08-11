package com.example.httpchatclient.chatpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.httpchatclient.DateUtils
import com.example.httpchatclient.R
import com.example.httpchatserver.database.message.Message
import kotlinx.android.synthetic.main.chat_received.view.*
import kotlinx.android.synthetic.main.chat_sent.view.*

class ChatRecyclerView(private val presenter: ChatPagePresenterImpl) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SentMessage(private var view: View) : RecyclerView.ViewHolder(view) {
        fun fillValues(message: Message) {
            view.sentChatText.text = message.messageText
            view.sentChatTime.text = DateUtils.getDateText(message.sendTime)
        }
    }

    class ReceivedMessage(private var view: View) : RecyclerView.ViewHolder(view) {
        fun fillValues(message: Message) {
            view.receivedChatText.text = message.messageText
            view.receivedChatTime.text = DateUtils.getDateText(message.sendTime)
        }
    }

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
        return presenter.getEntries().size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SentMessage) {
            holder.fillValues(presenter.getEntries()[position])
        } else if (holder is ReceivedMessage) {
            holder.fillValues(presenter.getEntries()[position])
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (presenter.getEntries()[position].senderId == presenter.getCurrentUser().id) MessageType.SENDER.ordinal else MessageType.RECEIVER.ordinal


    fun setData(chatEntries: MutableList<Message>) {
        notifyDataSetChanged()
    }

    enum class MessageType {
        SENDER,
        RECEIVER
    }
}
