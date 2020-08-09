package com.example.httpchatserver.database.messagethread

import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.user.User


data class MessageThreadDTO(
    var id: Int = 0,
    var participant1: User,
    var participant2: User,
    var lastMessage: Message
)