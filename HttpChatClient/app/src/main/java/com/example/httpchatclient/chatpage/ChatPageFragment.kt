package com.example.httpchatclient.chatpage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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
import kotlinx.android.synthetic.main.message_compose_layout.view.*
import java.util.*

class ChatPageFragment : Fragment(), ChatPageContract.View {

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
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(-1)) {
                    showChatLoading()
                    presenter.getPagedMessagesByThread(onMessagesLoad)

                }
            }
        })
        (activity as AppCompatActivity).setSupportActionBar(view.chat_toolbar)
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar.setDisplayHomeAsUpEnabled(true)

        view.chat_toolbar.setNavigationOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }

        var imageString =
            if (messageThread.participant1.id == currentUser.id) messageThread.participant2.image else messageThread.participant1.image
        if (imageString != null && !imageString.isEmpty()) {
            view.chat_user_icon.setImageBitmap(getBitmapFromBase64(imageString))
        }

        view.chat_toolbar.title =
            if (messageThread.participant1.id == currentUser.id) messageThread.participant2.userName else messageThread.participant1.userName

        if (messageThread.id != 0) {
            presenter.getPagedMessagesByThread(onMessagesLoad)
        }

        view.sendButton.setOnClickListener {
            presenter.sendMessage(view.composeMessageField.text.toString(), onMessageSend)
            view.composeMessageField.text.clear()
        }


        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                presenter.getLatestMessagesByThread(onMessagesLoad)
            }
        }, 0, 2000)
        recyclerView.scrollToPosition(0)
        return view
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    private val onMessagesLoad: () -> Any = {
        viewAdapter.notifyDataSetChanged()
        hideChatLoading()
    }

    private val onMessageSend: () -> Any = {
        viewAdapter.notifyDataSetChanged()
        recyclerView.smoothScrollToPosition(0)
    }

    override fun showChatLoading() {
//        view?.findViewById<ProgressBar>(R.id.chatLoading)?.visibility = View.VISIBLE
    }

    override fun hideChatLoading() {
//        view?.findViewById<ProgressBar>(R.id.chatLoading)?.visibility = View.INVISIBLE
    }

    fun getBitmapFromBase64(imageString: String): Bitmap {
        val byteArray = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}