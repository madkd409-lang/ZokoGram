package com.zokogram.ui.chat

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LastSeenIndicator(lastSeenTimestamp: Long?) {
    val text = if (lastSeenTimestamp == null || lastSeenTimestamp == 0L) {
        "был(а) недавно"
    } else {
        val date = Date(lastSeenTimestamp * 1000L)
        val format = SimpleDateFormat("dd MMM в HH:mm", Locale("ru"))
        "был(а) ${format.format(date)}"
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
