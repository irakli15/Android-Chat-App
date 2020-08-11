package com.example.httpchatclient.chathistorypage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.httpchatclient.R
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import kotlinx.android.synthetic.main.fragment_chat_history_page.*
import kotlinx.android.synthetic.main.fragment_chat_history_page.view.*
import kotlinx.android.synthetic.main.searchbar.view.*
import java.util.*


class ChatHistoryPageFragment() : Fragment(), ChatHistoryPageContract.View {
    private lateinit var user: User
    private val presenter = ChatHistoryPagePresenter()
    private lateinit var viewAdapter: ChatHistoryRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private var timer = Timer(true)
    private var searching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_history_page, container, false)
        setUnfocusHandler(view)
        user = arguments?.getParcelable("user")!!

        var viewManager = LinearLayoutManager(context)
        viewAdapter = ChatHistoryRecyclerViewAdapter(findNavController(), user)
        recyclerView = view.chatHistoryRecyclerView

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        presenter.loadAllMessageThreadsByUser(user, onMessageThreadsLoad)

        view.searchbarField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                presenter.searchMessageThreads(user, p0.toString(), onMessageThreadsLoad)
                searching = !p0.toString().isEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        timer = Timer(true)
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if(!searching) {
                    presenter.loadAllMessageThreadsByUser(user, onMessageThreadsLoad)
                }
            }
        }, 0, 2000)
        setUpItemTouchHelper()
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUnfocusHandler(view: View) {
        view.setOnTouchListener { v, event ->
            v.clearFocus()
            val imm =
                v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
            true
        }
    }

    private val onMessageThreadsLoad: (MutableList<MessageThread>) -> Any = {
        if (it.isEmpty()) {
            showEmptyMessageThreadsText()
        } else {
            hideEmptyMessageThreadsText()
        }
        Log.d("size", "${it.size}")
        viewAdapter.setData(it)
    }

    override fun showEmptyMessageThreadsText() {
        noMessagesText.visibility = View.VISIBLE
    }

    override fun hideEmptyMessageThreadsText() {
        noMessagesText.visibility = View.INVISIBLE
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }


    private fun setUpItemTouchHelper() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val swipedPosition = viewHolder.adapterPosition
                    val adapter = recyclerView.getAdapter() as ChatHistoryRecyclerViewAdapter
                    presenter.deleteMessageThread(adapter.entries[swipedPosition])
                    adapter.remove(swipedPosition)
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


}