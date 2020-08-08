package com.example.httpchatserver.database.message

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.user.User
import kotlinx.android.parcel.Parcelize
import java.time.DateTimeException
import java.util.*

@Parcelize
@Entity
data class Message (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var messageText: String,
    var sendTime: Date,
    var messageThreadId: Int
) : Parcelable