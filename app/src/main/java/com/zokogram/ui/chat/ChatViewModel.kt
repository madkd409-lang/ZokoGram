package com.zokogram.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zokogram.data.local.dao.EditedMessagesDao
import com.zokogram.data.local.entity.EditedMessageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val editedMessagesDao: EditedMessagesDao
) : ViewModel() {

    private val _chatId = MutableStateFlow(1L)
    val chatId: StateFlow<Long> = _chatId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val editedMessageIds: StateFlow<List<Long>> = _chatId
        .flatMapLatest { id -> editedMessagesDao.observeEditedIds(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedMessageForHistory = MutableStateFlow<Pair<Long, Long>?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val messageVersions: StateFlow<List<EditedMessageEntity>> = _selectedMessageForHistory
        .flatMapLatest { pair ->
            if (pair == null) flowOf(emptyList())
            else editedMessagesDao.observeVersions(pair.first, pair.second)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setChatId(id: Long) {
        _chatId.value = id
    }

    fun showHistory(chatId: Long, messageId: Long) {
        _selectedMessageForHistory.value = chatId to messageId
    }

    fun dismissHistory() {
        _selectedMessageForHistory.value = null
    }
}
