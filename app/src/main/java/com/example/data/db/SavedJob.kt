package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_jobs")
data class SavedJob(
    @PrimaryKey val jobId: String,
    val title: String,
    val company: String,
    val location: String,
    val type: String, // Full-time, Internship, Hybrid
    val salary: String,
    val status: String, // Bookmarked, Applied, Tech Round, Offer
    val dateApplied: Long = System.currentTimeMillis()
)
