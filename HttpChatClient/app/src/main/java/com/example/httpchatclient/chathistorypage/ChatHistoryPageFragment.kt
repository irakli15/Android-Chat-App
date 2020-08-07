package com.example.httpchatclient.chathistorypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.httpchatclient.Message
import com.example.httpchatclient.R
import com.example.httpchatclient.ServerRestAPI
import kotlinx.android.synthetic.main.fragment_chat_history_page.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatHistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_history_page, container, false)
        var viewManager = LinearLayoutManager(context)
        var viewAdapter = ChatHistoryRecyclerView(findNavController())

        var recyclerView: RecyclerView = view.chatHistoryRecyclerView

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }


        val serverRestAPI: ServerRestAPI = getServiceApi("localhost:5000/")
        serverRestAPI.getMessages("test").enqueue(object: Callback<Message>{
            override fun onFailure(call: Call<Message>, t: Throwable) {
                Log.d("response", "failed")
            }

            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                Log.d("response", "${response.body()?.messageText}" )
            }
        })
        // Inflate the layout for this fragment
        return view
    }

    private inline fun getServiceApi(baseUrl: String): ServerRestAPI {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ServerRestAPI::class.java)
    }
}