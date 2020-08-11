package com.example.httpchatclient.chathistorypage

import android.util.Log
import com.example.httpchatclient.ServerAPIClient
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import retrofit2.Call
import retrofit2.Response

class ChatHistoryPageModelImpl : ChatHistoryPageContract.Model {
    private var restClient = ServerAPIClient.getInstance()

    override fun getAllMessageThreadsByUser(
        user: User,
        onMessageThreadsLoad: (MutableList<MessageThread>) -> Any
    ) {
        restClient.getMessageThreadsByUser(user).enqueue(
            object : retrofit2.Callback<MutableList<MessageThread>> {
                override fun onFailure(call: Call<MutableList<MessageThread>>, t: Throwable) {
                    Log.d("failedResponse", t.message.toString())
                }

                override fun onResponse(
                    call: Call<MutableList<MessageThread>>,
                    response: Response<MutableList<MessageThread>>
                ) {
                    response.body()?.let { onMessageThreadsLoad(it) }
                }
            })
    }

    override fun searchMessageThreads(
        user: User,
        query: String,
        onMessageThreadsLoad: (MutableList<MessageThread>) -> Any
    ) {
        restClient.searchMessageThreads(user, query).enqueue(
            object : retrofit2.Callback<MutableList<MessageThread>> {
                override fun onFailure(call: Call<MutableList<MessageThread>>, t: Throwable) {
                    Log.d("failedResponse", t.message.toString())
                }

                override fun onResponse(
                    call: Call<MutableList<MessageThread>>,
                    response: Response<MutableList<MessageThread>>
                ) {
                    response.body()?.let { onMessageThreadsLoad(it) }
                }
            })
    }

    override fun deleteMessageThread(messageThread: MessageThread) {
        restClient.deleteMessageThread(messageThread.id).enqueue(
            object : retrofit2.Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Log.d("failedResponse", t.message.toString())
                }

                override fun onResponse(
                    call: Call<Any>,
                    response: Response<Any>
                ) {
                }
            })
    }
}