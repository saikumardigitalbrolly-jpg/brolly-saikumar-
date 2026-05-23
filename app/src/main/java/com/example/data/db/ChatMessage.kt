package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: String, // e.g. "mentor" or "course_python"
    val senderName: String, // e.g. "You" or "Brolly AI"
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
