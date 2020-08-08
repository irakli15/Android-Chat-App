package com.example.httpchatserver.database.messagethread

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.httpchatserver.database.user.User
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class MessageThread(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var participantId1: Int,
    var participantId2: Int
) : Parcelable