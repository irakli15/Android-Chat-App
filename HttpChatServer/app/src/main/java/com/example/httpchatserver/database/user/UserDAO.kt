package com.example.httpchatserver.database.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {
    @Query("select * from User where id=:userId")
    fun getUserById(userId: Int): User

    @Query("select * from User")
    fun getAllUsers(): MutableList<User>

    @Query("select * from User where userName = :userName")
    fun getUserByUserName(userName: String) : User

    @Insert
    fun insertUser(user: User): Long

    @Delete
    fun deleteUser(user: User)
}