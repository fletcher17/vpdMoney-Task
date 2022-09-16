package com.example.vpdmoneytask.repository

import com.example.vpdmoneytask.api.RetrofitInstance
import com.example.vpdmoneytask.db.UserDatabase
import com.example.vpdmoneytask.model.UserDetailsResponseItem

class UserRepository(val db: UserDatabase) {

    suspend fun getUserDetailsFromApi() =
        RetrofitInstance.api.getUserDetails()

    suspend fun update(userDetails: UserDetailsResponseItem) = db.getUserDetailDao().update(userDetails)

    fun getSavedDetails() = db.getUserDetailDao().getAllUsers()

    suspend fun deleteUser(user: UserDetailsResponseItem) = db.getUserDetailDao().deleteUser(user)

}