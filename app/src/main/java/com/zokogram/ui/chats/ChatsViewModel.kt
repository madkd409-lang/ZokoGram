package com.zokogram.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zokogram.data.tdlib.TdLibClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val tdClient: TdLibClient
) : ViewModel() {

    private val _chats = MutableStateFlow<List<TdApi.Chat>>(emptyList())
    val chats: StateFlow<List<TdApi.Chat>> = _chats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadChats()
    }

    fun loadChats() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Запрашиваем основные чаты из TDLib (первые 50 штук)
                val result = tdClient.send(TdApi.LoadChats(TdApi.ChatListMain(), 50))
                // После LoadChats чаты приходят через апдейты, либо можем запросить их локально
                fetchCachedChats()
            } catch (e: Exception) {
                // Обработка ошибки загрузки
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchCachedChats() {
        viewModelScope.launch {
            try {
                // Получаем список ID чатов и запрашиваем их объекты
                val chatsResult = tdClient.send(TdApi.GetChats(TdApi.ChatListMain(), 50))
                val chatList = mutableListOf<TdApi.Chat>()
                for (chatId in chatsResult.chatIds) {
                    val chat = tdClient.send(TdApi.GetChat(chatId))
                    chatList.add(chat)
                }
                _chats.value = chatList
            } catch (e: Exception) {
                // Игнорируем или логируем
            }
        }
    }
}
