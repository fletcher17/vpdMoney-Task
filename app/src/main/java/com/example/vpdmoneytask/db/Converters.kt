package com.example.vpdmoneytask.db

import androidx.room.TypeConverter
import com.example.vpdmoneytask.model.Company

class Converters {

    @TypeConverter
    fun fromCompany(company: Company): String? {
        return company.name
    }

    @TypeConverter
    fun toCompany(name: String): Company {
        return Company(name, name, name)
    }
}