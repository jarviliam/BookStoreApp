package com.example.googlebooksapi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.googlebooksapi.database.DAO.BookDAO

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class StoreDatabase : RoomDatabase(){
    abstract fun BookDao():    BookDAO

    companion object {
            fun buildDatabase(context: Context) : StoreDatabase {
                return Room.databaseBuilder(context,StoreDatabase::class.java,"STOREDB").build()
            }
    }
}