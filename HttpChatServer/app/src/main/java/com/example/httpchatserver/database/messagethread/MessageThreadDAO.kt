package com.example.httpchatserver.database.messagethread

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.httpchatserver.database.user.User

@Dao
interface MessageThreadDAO {
    @Query("select * from MessageThread where participantId1 = :userId or participantId2 = :userId")
    fun getMessageThreadByUser(userId: Int) : MutableList<MessageThread>

    @Insert
    fun insertMessageThread(messageThread: MessageThread) : Long

    @Delete
    fun deleteMessageThread(messageThread: MessageThread)
}