package com.example.googlebooksapi.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val imgURL: String,
    val language: String,
    val description: String,
    val pageCount : Int,
    //val epubAvailable: Boolean,
    //val pdfAvailable: Boolean

)
