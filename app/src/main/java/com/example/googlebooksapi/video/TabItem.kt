package com.example.googlebooksapi.video

data class TabItem(val id: Int, val title: String, val mainGenre: Genre, val subGenres: List<Genre>)

data class Tab(val type: Genre, val text: String)

enum class Genre {
    Action,
    Comedy,
    Thriller,
    Romance
}