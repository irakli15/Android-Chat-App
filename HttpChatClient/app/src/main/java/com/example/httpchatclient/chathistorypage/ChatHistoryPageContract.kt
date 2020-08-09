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
    }

    interface Presenter {
        fun loadAllMessageThreadsByUser(
            user: User,
            onMessageThreadsLoad: (MutableList<MessageThread>) -> Any
        )
    }
}