package com.zokogram.data.tdlib

import com.zokogram.data.spy.SpyUpdateHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TdLibManager @Inject constructor(
    private val spyUpdateHandler: SpyUpdateHandler
) {
    // Здесь будет инициализация и запуск TDLib клиента
    fun initClient() {
        // Заглушка для подключения к TDLib сессии
    }

    // Метод, который будет вызываться при получении апдейта о редактировании сообщения из TDLib
    fun handleMessageEdited(chatId: Long, messageId: Long, newText: String) {
        spyUpdateHandler.onMessageEdited(chatId, messageId, newText)
    }

    // Метод для обработки изменения статуса пользователя (Last Seen)
    fun handleUserStatusChanged(userId: Long, lastSeen: Long) {
        spyUpdateHandler.onUserStatusChanged(userId, lastSeen)
    }
}
