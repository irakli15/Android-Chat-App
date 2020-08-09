package com.example.httpchatclient.chatpage;

import com.example.httpchatserver.database.message.Message;

interface ChatPageContract {
    interface View {

    }

    interface Model {

        fun getAllMessagesByThread(
                threadId:Int,
                onMessagesLoad:(MutableList<Message>) -> Any
        )
    }

    interface Presenter {

    }
}
