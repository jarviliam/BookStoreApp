package com.example.googlebooksapi.injection

import android.content.Context
import com.example.googlebooksapi.database.StoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) : StoreDatabase = StoreDatabase.buildDatabase(context)
}