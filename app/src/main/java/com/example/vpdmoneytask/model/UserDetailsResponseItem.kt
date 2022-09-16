package com.example.vpdmoneytask.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "userDetails"
)
data class UserDetailsResponseItem(
    val address: Address? = null,
    val company: Company? = null,
    val email: String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String? = null,
    val phone: String? = null,
    val username: String? = null,
    val website: String? = null
)