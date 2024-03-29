package com.example.httpchatserver.database.messagethread

import android.os.Parcelable
import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.user.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageThread(
    var id: Int = 0,
    var participant1: User,
    var participant2: User,
    var lastMessage: Message
) : Parcelable