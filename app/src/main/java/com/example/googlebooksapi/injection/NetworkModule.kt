package com.example.googlebooksapi.injection

import com.example.googlebooksapi.aou.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHtpp(): OkHttpClient = OkHttpClient.Builder().build()


    @Provides
    @Singleton
    fun provideRetro(Ok: OkHttpClient) =
        Retrofit.Builder().client(Ok).addConverterFactory(MoshiConverterFactory.create()).baseUrl(
            "https://www.googleapis.com/books/v1/"
        ).build()

    @Provides
    @Singleton
    fun provideService(retro: Retrofit): ApiService = retro.create(ApiService::class.java)
}