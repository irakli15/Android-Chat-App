package com.example.httpchatserver.database.messagethread

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageThread(
    var id: Int = 0,
    var participantId1: Int,
    var participantId2: Int
) : Parcelable