package com.zokogram.data.local.entity

import androidx.room.Entity

@Entity(tableName = "edited_messages", primaryKeys = ["chatId", "messageId", "timestamp"])
data class EditedMessageEntity(
    val chatId: Long,
    val messageId: Long,
    val oldText: String,
    val newText: String,
    val timestamp: Long
)
