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
        threadId: Int,
        onMessagesLoad: (MutableList<Message>) -> Any
    ) {
        model.getAllMessagesByThread(threadId, onMessagesLoad)
    }

    override fun sendMessage(messageText: String, onMessageSend: (Message) -> Any) {
        model.saveMessage(
            Message(
                0,
                messageText,
                Date(),
                messageThread.id,
                currentUser.id,
                if (messageThread.participant1.id == currentUser.id) messageThread.participant2.id else messageThread.participant1.id
            ),
            onMessageSendPresenter,
            onMessageSend
        )
    }

    private val onMessageSendPresenter: (Message, (Message) -> Any) -> Any =
        { message: Message, onMessageSend: (Message) -> Any ->
            if (messageThread.id == 0 && message.messageThreadId != 0) {
                model.getMessageThreadById(message, onMessageSend)
            } else {
                onMessageSend(message)
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

