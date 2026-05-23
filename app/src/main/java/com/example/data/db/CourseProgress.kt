package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_progress")
data class CourseProgress(
    @PrimaryKey val courseId: String,
    val title: String,
    val category: String,
    val enrolled: Boolean = false,
    val progressPercent: Int = 0,
    val completedLessons: String = "" // Comma-separated indices, e.g. "0,1"
)
