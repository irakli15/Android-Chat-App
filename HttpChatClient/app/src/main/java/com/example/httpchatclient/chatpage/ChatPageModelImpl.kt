package com.example.httpchatclient.chatpage

import android.util.Log
import com.example.httpchatclient.ServerAPIClient
import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import retrofit2.Call
import retrofit2.Response

class ChatPageModelImpl(
    val currentUser: User,
    var messageThread: MessageThread
) : ChatPageContract.Model {
    private var restClient = ServerAPIClient.getInstance()
    val entries = mutableListOf<Message>()
    private val PAGING_SIZE = 5
    private var loadingData = false
    override fun getAllMessagesByThread(onMessagesLoad: () -> Any) {
        restClient.getMessagesByThread(messageThread.id).enqueue(
            object : retrofit2.Callback<MutableList<Message>> {
                override fun onFailure(call: Call<MutableList<Message>>, t: Throwable) {
                    Log.d("failedResponse", t.message.toString())
                }

                override fun onResponse(
                    call: Call<MutableList<Message>>,
                    response: Response<MutableList<Message>>
                ) {
                    response.body()?.let {
                        entries.clear()
                        entries.addAll(it)
                        onMessagesLoad()
                    }
                }
            })
    }

    override fun getPagedMessagesByThread(onMessagesLoad: () -> Any) {
        if (loadingData) {
            return
        }
        loadingData = true
        Log.d("load", "started loading")
        restClient.getPagedMessagesByThread(
            messageThread.id,
            if (entries.lastIndex > 0) entries[entries.lastIndex].id else Int.MAX_VALUE,
            PAGING_SIZE
        ).enqueue(
            object : retrofit2.Callback<MutableList<Message>> {
                override fun onFailure(call: Call<MutableList<Message>>, t: Throwable) {
                    Log.d("failedResponse", t.message.toString())
                    loadingData = false
                }

                override fun onResponse(
                    call: Call<MutableList<Message>>,
                    response: Response<MutableList<Message>>
                ) {
                    response.body()?.let {
                        it.forEach {
                            if (!entries.contains(it)) {
                                entries.add(entries.size, it)
                            }
                        }
                        loadingData = false
                        onMessagesLoad()
                    }
                }
            })
    }

    override fun getLatestMessagesByThread(onMessagesLoad: () -> Any) {
        if (loadingData) {
            return
        }
        loadingData = true
        Log.d("load", "started loading")
        restClient.getLatestMessagesByThread(
            messageThread.id,
            if (entries.size > 0) entries[0].id else 0
        ).enqueue(
            object : retrofit2.Callback<MutableList<Message>> {
                override fun onFailure(call: Call<MutableList<Message>>, t: Throwable) {
                    Log.d("failedResponse", t.message.toString())
                    loadingData = false
                }

                override fun onResponse(
                    call: Call<MutableList<Message>>,
                    response: Response<MutableList<Message>>
                ) {
                    response.body()?.let {
                        it.sortBy {
                            it.id
                        }
                        it.forEach {
                            if (!entries.contains(it)) {
                                entries.add(0, it)
                            }
                        }
                    }
                    loadingData = false
                    onMessagesLoad()
                }
            })
    }

    override fun saveMessage(
        message: Message,
        onMessageSendPresenter: (Message, () -> Any) -> Any,
        onMessageSend: () -> Any
    ) {
        restClient.saveMessage(message).enqueue(
            object : retrofit2.Callback<Message> {
                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Log.d("failedResponse", t.message.toString())
                }

                override fun onResponse(
                    call: Call<Message>,
                    response: Response<Message>
                ) {
                    response.body()?.let {
                        entries.add(0, it)
                        onMessageSendPresenter(it, onMessageSend)
                    }
                }
            })
    }

    override fun getMessageThreadById(
        message: Message,
        onMessageSend: () -> Any
    ) {
        restClient.getMessageThreadById(message.messageThreadId).enqueue(
            object : retrofit2.Callback<MessageThread> {
                override fun onFailure(call: Call<MessageThread>, t: Throwable) {
                    Log.d("failedResponse", t.message.toString())
                }

                override fun onResponse(
                    call: Call<MessageThread>,
                    response: Response<MessageThread>
                ) {
                    response.body()?.let {
                        messageThread = it
                        onMessageSend()
                    }
                }
            })

    }

}
