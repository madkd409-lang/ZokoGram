package com.zokogram.data.config

import com.zokogram.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfig @Inject constructor() {
    init {
        validateConfig()
    }

    private fun validateConfig() {
        require(BuildConfig.TG_API_ID != 0L && BuildConfig.TG_API_HASH.isNotBlank()) {
            "CRITICAL ERROR: Telegram API credentials (TG_API_ID / TG_API_HASH) are missing! " +
                    "Please configure them in local.properties before building ZokoGram."
        }
    }
}
