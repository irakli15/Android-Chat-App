package com.example.httpchatclient.chatpage;

import com.example.httpchatserver.database.message.Message

interface ChatPageContract {
    interface View {
        fun showChatLoading()
        fun hideChatLoading()
    }

    interface Model {

        fun getAllMessagesByThread(onMessagesLoad: () -> Any)

        fun getPagedMessagesByThread(onMessagesLoad: () -> Any)

        fun getLatestMessagesByThread(onMessagesLoad: () -> Any)


        fun saveMessage(
            message: Message,
            onMessageSendPresenter: (Message, () -> Any) -> Any,
            onMessageSend: () -> Any
        )

        fun getMessageThreadById(
            message: Message,
            onMessageSend: () -> Any
        )



    }

    interface Presenter {
        fun loadAllMessagesByThread(onMessagesLoad: () -> Any)
        fun getPagedMessagesByThread(onMessagesLoad: () -> Any)
        fun sendMessage(messageText: String, onMessageSent: () -> Any)
        fun getLatestMessagesByThread(onMessagesLoad: () -> Any)

    }
}
