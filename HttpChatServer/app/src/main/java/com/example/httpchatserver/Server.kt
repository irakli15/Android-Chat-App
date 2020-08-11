package com.example.httpchatserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.httpchatserver.database.message.Message
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
            mHttpServer!!.createContext("/getMessagesByThread", getMessagesByThread)
            mHttpServer!!.createContext("/getPagedMessagesByThread", getPagedMessagesByThread)
            mHttpServer!!.createContext("/saveMessage", saveMessage)
            mHttpServer!!.createContext("/getMessageThreadById", getMessageThreadById)
            mHttpServer!!.createContext("/getLatestMessagesByThread", getLatestMessagesByThread)
            mHttpServer!!.createContext("/deleteMessageThread", deleteMessageThread)

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

    private val getMessagesByThread = HttpHandler { httpExchange ->
        when (httpExchange!!.requestMethod) {
            "POST" -> {
                val threadId = queryToMap(httpExchange.requestURI.query).get("threadId")

                GlobalScope.launch {
                    val messageThreads =
                        threadId?.toInt()?.let { model.getMessagesByThread(it) }
                    sendResponse(
                        httpExchange,
                        Gson().toJson(messageThreads)
                    )
                }
            }
        }
    }

    private val getPagedMessagesByThread = HttpHandler { httpExchange ->
        when (httpExchange!!.requestMethod) {
            "POST" -> {
                val parameters = queryToMap(httpExchange.requestURI.query)
                val threadId = parameters.get("threadId")?.toInt()
                val currentId = parameters.get("currentId")?.toInt()
                val pagingSize = parameters.get("pagingSize")?.toInt()

                GlobalScope.launch {
                    if (threadId != null && currentId != null && pagingSize != null) {
                        val messages =
                            model.getPagedMessagesByThread(threadId, currentId, pagingSize)
                        sendResponse(
                            httpExchange,
                            Gson().toJson(messages)
                        )
                    }
                }
            }
        }
    }

    private val getLatestMessagesByThread = HttpHandler { httpExchange ->
        when (httpExchange!!.requestMethod) {
            "POST" -> {
                val parameters = queryToMap(httpExchange.requestURI.query)
                val threadId = parameters.get("threadId")?.toInt()
                val currentId = parameters.get("currentId")?.toInt()

                GlobalScope.launch {
                    if (threadId != null && currentId != null) {
                        val messages =
                            model.getLatestMessagesByThread(threadId, currentId)
                        sendResponse(
                            httpExchange,
                            Gson().toJson(messages)
                        )
                    }
                }
            }
        }
    }

    private val saveMessage = HttpHandler { httpExchange ->
        when (httpExchange!!.requestMethod) {
            "POST" -> {
                val jsonString = streamToString(httpExchange.requestBody)
                val message = Gson().fromJson(jsonString, Message::class.java)

                GlobalScope.launch {
                    sendResponse(
                        httpExchange,
                        Gson().toJson(model.insertMessage(message))
                    )
                }
            }
        }
    }

    private val getMessageThreadById = HttpHandler { httpExchange ->
        when (httpExchange!!.requestMethod) {
            "POST" -> {
                val threadId = queryToMap(httpExchange.requestURI.query).get("threadId")

                GlobalScope.launch {
                    val messageThread =
                        threadId?.toInt()?.let { model.getMessageThreadDTOById(threadId) }
                    sendResponse(
                        httpExchange,
                        Gson().toJson(messageThread)
                    )
                }
            }
        }
    }

    private val deleteMessageThread = HttpHandler { httpExchange ->
        when (httpExchange!!.requestMethod) {
            "DELETE" -> {
                val jsonString = streamToString(httpExchange.requestBody)
                val threadId = queryToMap(httpExchange.requestURI.query).get("threadId")?.toInt()

                GlobalScope.launch {
                    val messageThreads =
                        threadId?.let { model.deleteMessageThread(threadId) }
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