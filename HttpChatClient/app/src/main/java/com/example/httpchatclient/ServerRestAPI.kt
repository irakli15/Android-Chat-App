package com.example.httpchatclient

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerRestAPI {
    @GET("messages")
    fun getMessages(@Query("testQuery") testQuery: String) : Call<Message>
}