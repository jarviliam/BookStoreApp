package com.example.googlebooksapi.database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.googlebooksapi.database.BookEntity

@Dao
interface BookDAO {
    @Query("SELECT * FROM books")
    fun getAllBooks(): List<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBook(bookEntity: BookEntity)
}