package com.zokogram.data.auth

import com.zokogram.data.cache.MessageCache
import com.zokogram.data.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionCleaner @Inject constructor(
    private val database: AppDatabase,
    private val messageCache: MessageCache
) {
    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        try {
            // Очищаем таблицы Room
            database.clearAllTables()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Очищаем кэш в памяти
        messageCache.clear()
    }
}
