package com.example.httpchatserver

import android.content.Context
import androidx.room.Room
import com.example.httpchatserver.database.MessagesDatabase
import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User

class ServerModelImpl(context: Context) : ServerContract.Model {
    var database: MessagesDatabase = Room.databaseBuilder(context, MessagesDatabase::class.java, "messages.db")
        .fallbackToDestructiveMigration()
        .build()

    override fun getUserById(userId: Int): User {
        return database.getUserDAO().getUserById(userId)
    }

    override fun getAllUsers(): MutableList<User> {
        return database.getUserDAO().getAllUsers()
    }

    override fun getUserByUserName(userName: String): User {
        return database.getUserDAO().getUserByUserName(userName)
    }

    override fun insertUser(user: User): Long {
        return database.getUserDAO().insertUser(user)
    }

    override fun deleteUser(user: User) {
        return database.getUserDAO().deleteUser(user)
    }

    override fun getMessageThreadByUser(userId: Int): MutableList<MessageThread> {
        return database.getMessageThreadDAO().getMessageThreadByUser(userId)
    }

    override fun insertMessageThread(messageThread: MessageThread): Long {
        return database.getMessageThreadDAO().insertMessageThread(messageThread)
    }

    override fun deleteMessageThread(messageThread: MessageThread) {
        deleteMessageByThreadId(messageThread.id)
        return database.getMessageThreadDAO().deleteMessageThread(messageThread)
    }

    override fun getMessageByThread(threadId: Int): MutableList<Message> {
        return database.getMessageDAO().getMessageByThread(threadId)
    }

    override fun insertMessage(message: Message): Long {
        return database.getMessageDAO().insertMessage(message)
    }

    override fun deleteMessage(message: Message) {
        database.getMessageDAO().deleteMessage(message)
    }

    override fun deleteMessageByThreadId(threadId: Int){
        database.getMessageDAO().deleteMessagesByThreadId(threadId)
    }


}