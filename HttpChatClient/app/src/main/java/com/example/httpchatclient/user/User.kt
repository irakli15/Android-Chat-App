package com.example.httpchatserver.database.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var userName: String = "",
    var whatIDo: String = "",
    var image: String = ""
) : Parcelable