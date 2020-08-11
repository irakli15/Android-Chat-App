package com.example.httpchatclient.chatpage

import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import java.util.*

class ChatPagePresenterImpl(
    private val currentUser: User,
    private var messageThread: MessageThread
) : ChatPageContract.Presenter {
    private val model = ChatPageModelImpl(currentUser, messageThread)

    override fun loadAllMessagesByThread(
        onMessagesLoad: () -> Any
    ) {
        model.getAllMessagesByThread(onMessagesLoad)
    }

    override fun getPagedMessagesByThread(onMessagesLoad: () -> Any) {
        model.getPagedMessagesByThread(onMessagesLoad)
    }

    override fun sendMessage(messageText: String, onMessageSend: () -> Any) {
        model.saveMessage(
            Message(
                0,
                messageText,
                Date(),
                model.messageThread.id,
                currentUser.id,
                if (model.messageThread.participant1.id == currentUser.id) model.messageThread.participant2.id else model.messageThread.participant1.id
            ),
            onMessageSendPresenter,
            onMessageSend
        )
    }

    override fun getLatestMessagesByThread(onMessagesLoad: () -> Any) {
        model.getLatestMessagesByThread(onMessagesLoad)
    }

    private val onMessageSendPresenter: (Message, () -> Any) -> Any =
        { message: Message, onMessageSend: () -> Any ->
            if (model.messageThread.id == 0 && message.messageThreadId != 0) {
                model.getMessageThreadById(message, onMessageSend)
            } else {
                onMessageSend()
            }
        }


    fun getCurrentUser(): User {
        return model.currentUser
    }

    fun getMessageThread(): MessageThread {
        return model.messageThread
    }

    fun getEntries(): MutableList<Message> {
        return model.entries
    }
}

