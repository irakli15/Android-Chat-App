package com.example.httpchatclient

import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerRestAPI {
    @GET("messages")
    fun getMessages(): Call<Message>

    @POST("getUser")
    fun getUser(@Body user: User): Call<User>

    @POST("getMessageThreadsByUser")
    fun getMessageThreadsByUser(@Body user: User): Call<MutableList<MessageThread>>

    @POST("searchMessageThreads")
    fun searchMessageThreads(@Body user: User, @Query("query") query: String): Call<MutableList<MessageThread>>


}