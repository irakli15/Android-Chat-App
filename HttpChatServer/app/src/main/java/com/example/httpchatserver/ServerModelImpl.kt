package com.example.httpchatserver

import android.content.Context
import androidx.room.Room
import com.example.httpchatserver.database.MessagesDatabase
import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.messagethread.MessageThreadDTO
import com.example.httpchatserver.database.user.User
import java.util.*

class ServerModelImpl(context: Context) : ServerContract.Model {
    var database: MessagesDatabase =
        Room.databaseBuilder(context, MessagesDatabase::class.java, "messages.db")
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

    override fun getMessageThreadsByUser(userId: Int): MutableList<MessageThread> {
        return database.getMessageThreadDAO().getMessageThreadByUser(userId)
    }

    override fun getMessageThreadDTOsByUser(userId: Int): MutableList<MessageThreadDTO> {
        val messageThreads = getMessageThreadsByUser(userId)
        return messageThreads.map {
            messageThreadToDTO(it)
        }.toMutableList()
    }

    override fun searchMessageThreadDTOs(
        userId: Int,
        query: String
    ): MutableList<MessageThreadDTO> {
        val messageThreads = getMessageThreadsByUser(userId)

        val result = messageThreads.filter {
            searchMessages(it.id, query) > 0L
        }.map {
            messageThreadToDTO(it)
        }.toMutableList()

        if (!query.isEmpty()) {
            result.addAll(getNewMessageThreads(userId))
        }
        return result
    }

    private fun getNewMessageThreads(userId: Int): MutableList<MessageThreadDTO> {
        val currentUser = getUserById(userId)
        return getAllUsers().filter {
            it.id != userId
        }.map {
            MessageThreadDTO(0, currentUser, it, Message(0, "Start Messaging", Date(), 0))
        }.toMutableList()
    }

    private fun messageThreadToDTO(messageThread: MessageThread): MessageThreadDTO {
        val participant1 = getUserById(messageThread.participantId1)
        val participant2 = getUserById(messageThread.participantId2)

        val lastMessage = getLastMessageByThread(messageThread.id)
        return MessageThreadDTO(
            messageThread.id,
            participant1,
            participant2,
            lastMessage
        )
    }

    override fun insertMessageThread(messageThread: MessageThread): Long {
        return database.getMessageThreadDAO().insertMessageThread(messageThread)
    }

    override fun deleteMessageThread(messageThread: MessageThread) {
        deleteMessageByThreadId(messageThread.id)
        return database.getMessageThreadDAO().deleteMessageThread(messageThread)
    }

    override fun getMessageByThread(threadId: Int): MutableList<Message> {
        return database.getMessageDAO().getMessagesByThread(threadId)
    }

    override fun insertMessage(message: Message): Long {
        return database.getMessageDAO().insertMessage(message)
    }

    override fun deleteMessage(message: Message) {
        database.getMessageDAO().deleteMessage(message)
    }

    override fun deleteMessageByThreadId(threadId: Int) {
        database.getMessageDAO().deleteMessagesByThreadId(threadId)
    }

    override fun getLastMessageByThread(threadId: Int): Message {
        return database.getMessageDAO().getLastMessageByThread(threadId)
    }

    override fun searchMessages(threadId: Int, query: String): Long {
        return database.getMessageDAO().searchMessages(threadId, "%${query}%")
    }


}