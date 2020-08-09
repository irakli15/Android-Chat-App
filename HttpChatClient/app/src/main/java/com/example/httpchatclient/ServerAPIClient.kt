package com.example.httpchatclient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerAPIClient {


    companion object Factory {
        private val BASE_URL: String = "http://10.0.2.2:5000/"

        private var serverRestAPI = getServiceApi(BASE_URL)

        fun getInstance(): ServerRestAPI {
            return serverRestAPI
        }

        private fun getServiceApi(baseUrl: String): ServerRestAPI {
//        val okHttpClient = OkHttpClient.Builder()
//            .connectTimeout(20, TimeUnit.SECONDS)
//            .writeTimeout(20, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .build();

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
                .build()

            return retrofit.create(ServerRestAPI::class.java)
        }

    }

}