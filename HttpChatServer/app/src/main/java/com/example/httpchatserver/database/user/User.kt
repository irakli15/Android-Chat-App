package com.example.httpchatserver.database.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var userName: String,
    var whatIDo: String,
    var image: String
) : Parcelable