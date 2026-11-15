package com.zokogram.data.cache

import org.drinkless.tdlib.TdApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageCache @Inject constructor() {
    private val maxEntries = 5000
    private val cache = object : LinkedHashMap<Long, TdApi.Message>(100, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Long, TdApi.Message>): Boolean {
            return size > maxEntries
        }
    }

    @Synchronized
    fun put(messageId: TdApi.Message) {
        cache[messageId.id] = messageId
    }

    @Synchronized
    fun get(messageId: Long): TdApi.Message? {
        return cache[messageId]
    }

    @Synchronized
    fun clear() {
        cache.clear()
    }
}
