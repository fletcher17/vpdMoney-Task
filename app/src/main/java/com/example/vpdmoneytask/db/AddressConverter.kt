package com.example.vpdmoneytask.db

import androidx.room.TypeConverter
import com.example.vpdmoneytask.model.Address
import com.example.vpdmoneytask.model.Geo

class AddressConverter {

    @TypeConverter
    fun fromAddress(address: Address): String {
        return address.city
    }

    @TypeConverter
    fun toAddress(city: String): Address {
        return Address(city, city, city, city)
    }
}