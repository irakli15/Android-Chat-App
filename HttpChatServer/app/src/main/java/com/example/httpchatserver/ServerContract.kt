package com.example.httpchatserver

import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.messagethread.MessageThreadDTO
import com.example.httpchatserver.database.user.User

interface ServerContract {
    interface Model {
        fun getUserById(userId: Int): User
        fun getAllUsers(): MutableList<User>
        fun getUserByUserName(userName: String): User
        fun insertUser(user: User): Long
        fun deleteUser(user: User)

        fun getMessageThreadsByUser(userId: Int): MutableList<MessageThread>
        fun getMessageThreadDTOsByUser(userId: Int): MutableList<MessageThreadDTO>
        fun searchMessageThreadDTOs(userId: Int, query: String): MutableList<MessageThreadDTO>


            fun insertMessageThread(messageThread: MessageThread): Long
        fun deleteMessageThread(messageThread: MessageThread)

        fun getMessageByThread(threadId: Int): MutableList<Message>
        fun insertMessage(message: Message): Long
        fun deleteMessage(message: Message)
        fun deleteMessageByThreadId(threadId: Int)
        fun getLastMessageByThread(threadId: Int): Message
        fun searchMessages(threadId: Int, query: String): Long
    }
}