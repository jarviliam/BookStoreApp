package com.example.googlebooksapi.injection

import android.content.Context
import com.example.googlebooksapi.database.StoreDatabase
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) : StoreDatabase = StoreDatabase.buildDatabase(context)
}