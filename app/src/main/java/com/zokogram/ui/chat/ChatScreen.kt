package com.zokogram.ui.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zokogram.ui.components.EditedMessageHistoryDialog
import org.drinkless.tdlib.TdApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: Long,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel() // Используем готовый ChatViewModel из ранних шагов
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    var selectedMessageForHistory by remember { mutableStateOf<TdApi.Message?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Чат #$chatId") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (messages.isEmpty()) {
                Text(
                    "Сообщения загружаются или пусты...",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    reverseLayout = true
                ) {
                    items(messages, key = { it.id }) { message ->
                        MessageItem(
                            message = message,
                            onLongClick = { 
                                // При долгом клике открываем историю правок этого сообщения
                                selectedMessageForHistory = message 
                            }
                        )
                    }
                }
            }

            // Если пользователь запросил историю изменений конкретного сообщения — показываем шпионский диалог
            selectedMessageForHistory?.let { msg ->
                EditedMessageHistoryDialog(
                    messageId = msg.id,
                    onDismiss = { selectedMessageForHistory = null }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageItem(
    message: TdApi.Message,
    onLongClick: () -> Unit
) {
    val text = (message.content as? TdApi.MessageText)?.text?.text ?: "[Медиа / Другой контент]"
    val isEdited = message.editDate > 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Индикатор того, что сообщение было изменено (наш шпионский триггер)
                if (isEdited) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Изменено",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "изменено",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
