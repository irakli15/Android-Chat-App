package com.example.httpchatserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.httpchatserver.database.user.User
import com.google.gson.Gson
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.Executors

class Server : Service() {
    lateinit var model: ServerContract.Model
    override fun onCreate() {
        val port = 5000
        startServer(port)
        model = ServerModelImpl(applicationContext)
        GlobalScope.launch {
            model.getAllUsers()
        }
    }


    private fun streamToString(inputStream: InputStream): String {
        val s = Scanner(inputStream).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

    private fun sendResponse(httpExchange: HttpExchange, responseText: String) {
        httpExchange.sendResponseHeaders(200, responseText.length.toLong())
        val os = httpExchange.responseBody
        os.write(responseText.toByteArray())
        os.close()
    }

    private var mHttpServer: HttpServer? = null


    private fun startServer(port: Int) {
        try {
            mHttpServer = HttpServer.create(InetSocketAddress(port), 0)
            mHttpServer!!.executor = Executors.newCachedThreadPool()

            mHttpServer!!.createContext("/", rootHandler)
            mHttpServer!!.createContext("/index", rootHandler)
            // Handle /messages endpoint
            mHttpServer!!.createContext("/messages", messageHandler)

            mHttpServer!!.createContext("/getUser", getUserHandler)
            mHttpServer!!.createContext("/getMessageThreadsByUser", getMessageThreadsByUser)
            mHttpServer!!.createContext("/searchMessageThreads", searchMessageThreads)

            mHttpServer!!.start()//startServer server;
//            server_status.text = "server is running on port: {$port}"
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun stopServer() {
        if (mHttpServer != null) {
            mHttpServer!!.stop(0)
        }
    }

    // Handler for root endpoint
    private val rootHandler = HttpHandler { exchange ->
        run {
            // Get request method
            when (exchange!!.requestMethod) {
                "GET" -> {
                    sendResponse(exchange, "Welcome to my server")
                }

            }
        }

    }

    private val messageHandler = HttpHandler { httpExchange ->
        run {
            when (httpExchange!!.requestMethod) {
                "GET" -> {
                    // Get all messages
                }
                "POST" -> {
                    val inputStream = httpExchange.requestBody

                    val requestBody = streamToString(inputStream)
                    val jsonBody = JSONObject(requestBody)
                    // save message to database

                    //for testing
                    sendResponse(httpExchange, jsonBody.toString())

                }

            }
        }
    }


    private val getUserHandler = HttpHandler { httpExchange ->
        when (httpExchange!!.requestMethod) {
            "POST" -> {
                val jsonString = streamToString(httpExchange.requestBody)
                val user = Gson().fromJson(jsonString, User::class.java)

                GlobalScope.launch {
                    val userFromDatabase = model.getUserByUserName(user.userName)
                    if (userFromDatabase == null) {
                        user.id = model.insertUser(user).toInt()
                        sendResponse(httpExchange, Gson().toJson(user as User))
                    } else {
                        sendResponse(
                            httpExchange,
                            Gson().toJson(userFromDatabase)
                        )
                    }
                }

            }
        }
    }

    private val getMessageThreadsByUser = HttpHandler { httpExchange ->
        when (httpExchange!!.requestMethod) {
            "POST" -> {
                val jsonString = streamToString(httpExchange.requestBody)
                val user = Gson().fromJson(jsonString, User::class.java)

                GlobalScope.launch {
                    val messageThreads = model.getMessageThreadDTOsByUser(user.id)
                    sendResponse(
                        httpExchange,
                        Gson().toJson(messageThreads)
                    )
                }
            }
        }
    }

    private val searchMessageThreads = HttpHandler { httpExchange ->
        when (httpExchange!!.requestMethod) {
            "POST" -> {
                val jsonString = streamToString(httpExchange.requestBody)
                val user = Gson().fromJson(jsonString, User::class.java)
                var query = queryToMap(httpExchange.requestURI.query).get("query")

                GlobalScope.launch {
                    val messageThreads =
                        model.searchMessageThreadDTOs(user.id, if (query == null) "" else query)
                    sendResponse(
                        httpExchange,
                        Gson().toJson(messageThreads)
                    )
                }
            }
        }
    }

    fun queryToMap(query: String): Map<String, String> {
        val result: MutableMap<String, String> = HashMap()
        for (param in query.split("&".toRegex()).toTypedArray()) {
            val entry = param.split("=".toRegex()).toTypedArray()
            if (entry.size > 1) {
                result[entry[0]] = entry[1]
            } else {
                result[entry[0]] = ""
            }
        }
        return result
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}