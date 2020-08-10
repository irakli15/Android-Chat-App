package com.example.httpchatclient.chatpage;

import com.example.httpchatserver.database.message.Message

interface ChatPageContract {
    interface View {

    }

    interface Model {

        fun getAllMessagesByThread(
            threadId: Int,
            onMessagesLoad: (MutableList<Message>) -> Any
        )

        fun saveMessage(
            message: Message,
            onMessageSendPresenter: (Message, (Message) -> Any) -> Any,
            onMessageSend: (Message) -> Any
        )

        fun getMessageThreadById(
            message: Message,
            onMessageSend: (Message) -> Any
        )
    }

    interface Presenter {
        fun loadAllMessagesByThread(threadId: Int, onMessagesLoad: (MutableList<Message>) -> Any)
        fun sendMessage(messageText: String, onMessageSent: (Message) -> Any)

    }
}
