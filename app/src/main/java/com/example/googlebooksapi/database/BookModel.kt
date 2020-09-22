package com.example.googlebooksapi.database

import androidx.room.Entity

@Entity
data class BookModel(val id: String, val title: String, val author : String, val imgURL: String)
