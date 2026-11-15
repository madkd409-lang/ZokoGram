package com.zokogram.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zokogram.data.local.dao.EditedMessagesDao
import com.zokogram.data.local.dao.LastSeenDao
import com.zokogram.data.local.entity.EditedMessageEntity
import com.zokogram.data.local.entity.LastSeenEntity

@Database(entities = [EditedMessageEntity::class, LastSeenEntity::class], version = 1, exportSchema = false)
abstract class ZokoGramDatabase : RoomDatabase() {
    abstract fun editedMessagesDao(): EditedMessagesDao
    abstract fun lastSeenDao(): LastSeenDao
}
