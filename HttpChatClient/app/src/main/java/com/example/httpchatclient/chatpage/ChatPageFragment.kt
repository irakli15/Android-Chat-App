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
import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import kotlinx.android.synthetic.main.chat_toolbar_and_recyclerview.view.*
import kotlinx.android.synthetic.main.message_compose_layout.view.*
import java.util.*

class ChatPageFragment : Fragment() {

    private lateinit var currentUser: User
    private lateinit var messageThread: MessageThread
    private lateinit var presenter: ChatPagePresenterImpl
    private lateinit var viewAdapter: ChatRecyclerView
    private lateinit var recyclerView: RecyclerView
    private val timer = Timer(true)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_page, container, false)

        currentUser = arguments?.getParcelable("currentUser")!!
        messageThread = arguments?.getParcelable("messageThread")!!
        presenter = ChatPagePresenterImpl(currentUser, messageThread)
        val viewManager = LinearLayoutManager(context)
        viewManager.reverseLayout = true
        viewAdapter = ChatRecyclerView(presenter)
        view.appbar.setExpanded(false)
        recyclerView = view.chatRecyclerView
        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        (activity as AppCompatActivity).setSupportActionBar(view.chat_toolbar)
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar.setDisplayHomeAsUpEnabled(true)

        view.chat_toolbar.setNavigationOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }

        view.chat_toolbar.title =
            if (messageThread.participant1.id == currentUser.id) messageThread.participant2.userName else messageThread.participant1.userName

        if (messageThread.id != 0) {
            presenter.loadAllMessagesByThread(messageThread.id, onMessagesLoad)
        }

        view.sendButton.setOnClickListener {
            presenter.sendMessage(view.composeMessageField.text.toString(), onMessageSend)
            view.composeMessageField.text.clear()
        }


        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                presenter.loadAllMessagesByThread(messageThread.id, onMessagesLoad)
            }
        }, 0, 2000)
        recyclerView.scrollToPosition(0)
        return view
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    private val onMessagesLoad: (MutableList<Message>) -> Any = {
        viewAdapter.notifyDataSetChanged()
    }

    private val onMessageSend: (Message) -> Any = {
        viewAdapter.notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(0)
    }
}