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

    override fun getAllMessagesByThread(
        threadId: Int,
        onMessagesLoad: (MutableList<Message>) -> Any
    ) {
        restClient.getMessagesByThread(threadId).enqueue(
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
                        onMessagesLoad(it)
                    }
                }
            })
    }

    override fun saveMessage(
        message: Message,
        onMessageSendPresenter: (Message, (Message) -> Any) -> Any,
        onMessageSend: (Message) -> Any
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
        onMessageSend: (Message) -> Any
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
                        onMessageSend(message)
                    }
                }
            })

    }

}
