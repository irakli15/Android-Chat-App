package com.example.httpchatserver.database.message

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDAO {
    @Query("select * from Message where messageThreadId = :threadId")
    fun getMessagesByThread(threadId: Int): MutableList<Message>

    @Query("select * from Message where messageThreadId = :threadId order by id desc limit 1")
    fun getLastMessageByThread(threadId: Int): Message

    @Insert
    fun insertMessage(message: Message): Long

    @Delete
    fun deleteMessage(message: Message)

    @Query("delete from Message where messageThreadId = :threadId")
    fun deleteMessagesByThreadId(threadId: Int)

    @Query("select count(*) from Message where messageThreadId = :threadId and messageText like :query")
    fun searchMessages(threadId: Int, query: String) : Long
}