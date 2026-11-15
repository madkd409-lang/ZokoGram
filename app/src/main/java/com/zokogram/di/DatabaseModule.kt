package com.zokogram.di

import android.content.Context
import androidx.room.Room
import com.zokogram.data.local.ZokoGramDatabase
import com.zokogram.data.local.dao.EditedMessagesDao
import com.zokogram.data.local.dao.LastSeenDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ZokoGramDatabase {
        return Room.databaseBuilder(
            context,
            ZokoGramDatabase::class.java,
            "zokogram_database"
        ).build()
    }

    @Provides
    fun provideEditedMessagesDao(database: ZokoGramDatabase): EditedMessagesDao {
        return database.editedMessagesDao()
    }

    @Provides
    fun provideLastSeenDao(database: ZokoGramDatabase): LastSeenDao {
        return database.lastSeenDao()
    }
}
