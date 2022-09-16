package com.example.vpdmoneytask.api

import com.example.vpdmoneytask.model.UserDetailsResponse
import com.example.vpdmoneytask.model.UserDetailsResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface UsersApi {

    @GET("users")
    suspend fun getUserDetails(): Response<UserDetailsResponse>
}