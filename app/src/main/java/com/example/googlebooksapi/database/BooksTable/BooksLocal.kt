package com.example.googlebooksapi.database.BooksTable

import com.example.googlebooksapi.aou.ItemObj
import com.example.googlebooksapi.database.DAO.BookDAO
import com.example.googlebooksapi.database.StoreDatabase
import com.example.googlebooksapi.ext.toEntity
import javax.inject.Inject

class BooksLocal @Inject constructor(private val bookDAO: BookDAO) {

    suspend fun saveBookItem(book : ItemObj){
        return bookDAO.saveBook(book.toEntity())
    }
}