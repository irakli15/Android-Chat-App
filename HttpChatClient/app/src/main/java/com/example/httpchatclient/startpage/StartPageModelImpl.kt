package com.example.httpchatclient.startpage

import android.util.Log
import com.example.httpchatclient.ServerAPIClient
import com.example.httpchatserver.database.user.User
import retrofit2.Call
import retrofit2.Response

class StartPageModelImpl : StartPageContract.Model {

    private var restClient = ServerAPIClient.getInstance()

    override fun getUser(userName: String, onUserLoad: (user: User) -> Any) {
        restClient.getUser(User(userName = userName)).enqueue(
            object : retrofit2.Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("failedResponse", t.message.toString())
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    response.body()?.let { onUserLoad(it) }
                }
            })
    }

}