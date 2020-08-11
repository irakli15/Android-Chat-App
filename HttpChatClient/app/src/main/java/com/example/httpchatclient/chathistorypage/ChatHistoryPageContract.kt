package com.example.httpchatclient.chathistorypage

import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User

interface ChatHistoryPageContract {
    interface View {
        fun showEmptyMessageThreadsText()
        fun hideEmptyMessageThreadsText()
    }

    interface Model {
        fun getAllMessageThreadsByUser(
            user: User,
            onMessageThreadsLoad: (MutableList<MessageThread>) -> Any
        )

        fun searchMessageThreads(
            user: User,
            query: String,
            onMessageThreadsLoad: (MutableList<MessageThread>) -> Any
        )

        fun deleteMessageThread(messageThread: MessageThread)
    }

    interface Presenter {
        fun loadAllMessageThreadsByUser(
            user: User,
            onMessageThreadsLoad: (MutableList<MessageThread>) -> Any
        )

        fun searchMessageThreads(
            user: User,
            query: String,
            onMessageThreadsLoad: (MutableList<MessageThread>) -> Any
        )

        fun deleteMessageThread(messageThread: MessageThread)

    }
}