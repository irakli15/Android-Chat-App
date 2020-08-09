package com.example.httpchatserver.database.message

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Message (
    var id: Int = 0,
    var messageText: String,
    var sendTime: Date,
    var messageThreadId: Int,
    var senderId: Int,
    var receiverId: Int
) : Parcelable