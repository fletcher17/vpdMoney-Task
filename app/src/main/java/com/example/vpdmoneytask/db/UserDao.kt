package com.example.vpdmoneytask.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vpdmoneytask.model.UserDetailsResponseItem

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: UserDetailsResponseItem): Long

    @Query("SELECT * FROM userDetails")
    fun getAllUsers(): LiveData<List<UserDetailsResponseItem>>

    @Delete
    suspend fun deleteUser(user: UserDetailsResponseItem)

}