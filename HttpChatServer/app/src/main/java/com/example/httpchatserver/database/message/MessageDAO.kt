package com.example.httpchatserver.database.message

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDAO {
    @Query("select * from Message where messageThreadId = :threadId")
    fun getMessageByThread(threadId: Int): MutableList<Message>

    @Insert
    fun insertMessage(message: Message): Long

    @Delete
    fun deleteMessage(message: Message)

    @Query("delete from Message where messageThreadId = :threadId")
    fun deleteMessagesByThreadId(threadId: Int)
}