package com.example.googlebooksapi.aou

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("volumes")
    suspend fun getBooksById(
        @Query("id") id: String,
        @Query("key") apiKey: String
    ): Response<ResponseO>

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): Response<ResponseO>

}