package com.zokogram.data.spy

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageCache @Inject constructor() {
    private val cache = ConcurrentHashMap<Long, String>()

    fun get(messageId: Long): String? = cache[messageId]

    fun put(messageId: Long, text: String) {
        cache[messageId] = text
    }

    fun remove(messageId: Long) {
        cache.remove(messageId)
    }
}
