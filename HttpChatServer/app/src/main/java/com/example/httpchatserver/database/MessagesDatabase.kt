package com.example.httpchatserver.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.httpchatserver.database.message.Message
import com.example.httpchatserver.database.message.MessageDAO
import com.example.httpchatserver.database.messagethread.MessageThread
import com.example.httpchatserver.database.messagethread.MessageThreadDAO
import com.example.httpchatserver.database.user.User
import com.example.httpchatserver.database.user.UserDAO

@Database(entities = [User::class, MessageThread::class, Message::class], version = 1)
@TypeConverters(Converters::class)
abstract class MessagesDatabase : RoomDatabase() {
    abstract fun getUserDAO(): UserDAO
    abstract fun getMessageThreadDAO(): MessageThreadDAO
    abstract fun getMessageDAO(): MessageDAO
}