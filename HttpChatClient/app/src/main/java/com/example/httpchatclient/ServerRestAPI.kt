package com.example.httpchatclient

import retrofit2.Call
import retrofit2.http.GET

interface ServerRestAPI {
    @GET("messages")
    fun getMessages() : Call<Message>
}