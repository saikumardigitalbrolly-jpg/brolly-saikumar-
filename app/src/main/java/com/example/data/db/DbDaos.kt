package com.example.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseProgressDao {
    @Query("SELECT * FROM course_progress")
    fun getAllProgress(): Flow<List<CourseProgress>>

    @Query("SELECT * FROM course_progress WHERE courseId = :courseId LIMIT 1")
    suspend fun getProgressForCourse(courseId: String): CourseProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: CourseProgress)

    @Query("UPDATE course_progress SET progressPercent = :percent, completedLessons = :completed WHERE courseId = :courseId")
    suspend fun updateProgress(courseId: String, percent: Int, completed: String)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages WHERE sessionId = :sessionId")
    suspend fun clearSession(sessionId: String)
}

@Dao
interface SavedJobDao {
    @Query("SELECT * FROM saved_jobs ORDER BY dateApplied DESC")
    fun getAllSavedJobs(): Flow<List<SavedJob>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateJob(savedJob: SavedJob)

    @Delete
    suspend fun deleteJob(savedJob: SavedJob)

    @Query("UPDATE saved_jobs SET status = :status WHERE jobId = :jobId")
    suspend fun updateJobStatus(jobId: String, status: String)
}
