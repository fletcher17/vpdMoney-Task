package com.example.vpdmoneytask.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vpdmoneytask.model.UserDetailsResponseItem

@Database(
    entities = [UserDetailsResponseItem::class],
    version = 1
)
@TypeConverters(Converters::class, AddressConverter::class)
abstract class UserDatabase: RoomDatabase() {

    abstract fun getUserDetailDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "userDetail_db.db"
            ).build()
    }



}