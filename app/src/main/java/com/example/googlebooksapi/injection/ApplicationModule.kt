package com.example.googlebooksapi.injection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    //TODO: Use AndroidX Version
    @Provides
    fun provideSP(app: Application): SharedPreferences =
        app.getSharedPreferences("main", Context.MODE_PRIVATE)

    @Provides
    fun provideContext(app: Application): Context = app
}