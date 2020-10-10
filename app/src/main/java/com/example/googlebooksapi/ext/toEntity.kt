package com.example.googlebooksapi.ext

import com.example.googlebooksapi.aou.ItemObj
import com.example.googlebooksapi.database.BookEntity

fun ItemObj.toEntity(): BookEntity = BookEntity(
    this.id,
    this.volumeInfo.title,
    this.volumeInfo.authors[0],
    this.volumeInfo.imageLinks.thumbnail,
    this.volumeInfo.language,
    this.volumeInfo.description,
    this.volumeInfo.pageCount
)

