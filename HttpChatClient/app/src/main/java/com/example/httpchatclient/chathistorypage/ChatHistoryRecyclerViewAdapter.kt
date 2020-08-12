package com.example.httpchatclient.chathistorypage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.httpchatclient.DateUtils
import com.example.httpchatclient.R
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import kotlinx.android.synthetic.main.chat_history_entry.view.*

class ChatHistoryRecyclerViewAdapter(
    private val navController: NavController,
    private val currentUser: User
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ChatHistoryEntry(private var view: View) :
        RecyclerView.ViewHolder(view) {
        fun fillValues(messageThread: MessageThread) {
            view.historyUserNameField.text =
                if (messageThread.participant1.id == currentUser.id) messageThread.participant2.userName else messageThread.participant1.userName
            view.historyDateTimeField.text = messageThread.lastMessage.sendTime.toString()
            view.lastMessageText.text = messageThread.lastMessage.messageText
            view.setOnClickListener(View.OnClickListener {
                val args = Bundle()
                args.putParcelable("currentUser", currentUser)
                args.putParcelable("messageThread", messageThread)
                navController.navigate(R.id.action_chatHistoryFragment_to_chatPageFragment, args)
            })
            var imageString =
                if (messageThread.participant1.id == currentUser.id) messageThread.participant2.image else messageThread.participant1.image
            if (imageString != null && !imageString.isEmpty()) {
                view.historyUserImage.setImageBitmap(getBitmapFromBase64(imageString))
            }
            view.historyDateTimeField.text =
                if (messageThread.id == 0) "" else DateUtils.getDateText(messageThread.lastMessage.sendTime)
        }
    }

    val entries: MutableList<MessageThread> = mutableListOf()

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
        return entries.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChatHistoryEntry) {
            holder.fillValues(entries[position])
        }
    }

    fun setData(history: MutableList<MessageThread>) {
        entries.clear()
        entries.addAll(history)
        notifyDataSetChanged()
    }

    fun remove(swipedPosition: Int) {
        entries.removeAt(swipedPosition)
        notifyDataSetChanged()
    }

    fun getBitmapFromBase64(imageString: String): Bitmap {
        val byteArray = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
