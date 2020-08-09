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
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import kotlinx.android.synthetic.main.chat_toolbar_and_recyclerview.view.*

class ChatPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_page, container, false)

        val currentUser: User = arguments?.getParcelable("currentUser")!!
        val messageThread: MessageThread = arguments?.getParcelable("messageThread")!!

        val viewManager = LinearLayoutManager(context)
        viewManager.reverseLayout = true
        val viewAdapter = ChatRecyclerView(currentUser, messageThread)

        val recyclerView: RecyclerView = view.chatRecyclerView
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

        return view
    }
}