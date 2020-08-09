package com.example.httpchatclient.chatpage

import com.example.httpchatserver.database.message.Message

class ChatPageModelImpl: ChatPageContract.Model {
    override fun getAllMessagesByThread(
        threadId: Int,
        onMessagesLoad: (MutableList<Message>) -> Any
    ) {
        TODO("Not yet implemented")
    }

}