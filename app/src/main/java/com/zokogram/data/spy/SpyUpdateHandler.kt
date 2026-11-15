package com.zokogram.data.spy

import com.zokogram.data.local.dao.EditedMessagesDao
import com.zokogram.data.local.dao.LastSeenDao
import com.zokogram.data.local.entity.EditedMessageEntity
import com.zokogram.data.local.entity.LastSeenEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpyUpdateHandler @Inject constructor(
    private val messageCache: MessageCache,
    private val editedMessagesDao: EditedMessagesDao,
    private val lastSeenDao: LastSeenDao
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun onNewMessage(chatId: Long, messageId: Long, text: String) {
        messageCache.put(messageId, text)
    }

    fun onMessageEdited(chatId: Long, messageId: Long, newText: String) {
        val oldText = messageCache.get(messageId) ?: "Неизвестный оригинал"
        if (oldText != newText) {
            scope.launch {
                editedMessagesDao.insertEditedMessage(
                    EditedMessageEntity(
                        chatId = chatId,
                        messageId = messageId,
                        oldText = oldText,
                        newText = newText,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
            messageCache.put(messageId, newText)
        }
    }

    fun onUserStatusChanged(userId: Long, lastSeen: Long) {
        scope.launch {
            lastSeenDao.insertLastSeen(
                LastSeenEntity(
                    userId = userId,
                    lastSeenTimestamp = lastSeen
                )
            )
        }
    }
}
