package com.example.httpchatclient.chathistorypage

import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User

class ChatHistoryPagePresenter : ChatHistoryPageContract.Presenter {
    private var model = ChatHistoryPageModelImpl()

    override fun loadAllMessageThreadsByUser(
        user: User,
        onMessageThreadsLoad: (MutableList<MessageThread>) -> Any
    ) {
        model.getAllMessageThreadsByUser(user, onMessageThreadsLoad)
    }
}