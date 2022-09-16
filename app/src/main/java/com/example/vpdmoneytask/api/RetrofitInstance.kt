package com.example.vpdmoneytask.api

import com.example.vpdmoneytask.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }


        val api by lazy {
            retrofit.create(UsersApi::class.java)
        }
    }


}