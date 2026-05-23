package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BrollyData
import com.example.data.Course
import com.example.data.GeminiRepository
import com.example.data.JobOpening
import com.example.data.Lesson
import com.example.data.db.AppDatabase
import com.example.data.db.ChatMessage
import com.example.data.db.CourseProgress
import com.example.data.db.SavedJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BrollyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val progressDao = db.courseProgressDao()
    private val chatDao = db.chatMessageDao()
    private val jobDao = db.savedJobDao()
    private val geminiRepository = GeminiRepository()

    // Database flows with robust lifecycle survival
    val allProgress: StateFlow<List<CourseProgress>> = progressDao.getAllProgress()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedJobs: StateFlow<List<SavedJob>> = jobDao.getAllSavedJobs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val mentorChatMessages: StateFlow<List<ChatMessage>> = chatDao.getMessagesForSession("mentor")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Routing Active Tab
    val activeTab = mutableStateOf("home") // home, learn, mentor, jobs, profile

    // Active Course Video State
    val activePlayingCourse = mutableStateOf<Course?>(null)
    val activePlayingLessonIndex = mutableStateOf(0)

    // AI Mentor Loading
    val isMentorLoading = mutableStateOf(false)

    // Course doubt system loading
    val isCourseDoubtLoading = mutableStateOf(false)

    // ATS Evaluator variables
    val resumeTextState = mutableStateOf("")
    val isAtsScoring = mutableStateOf(false)
    val atsScoreResult = mutableStateOf<String?>(null)

    // Simulated Mock Interview loops
    val interviewType = mutableStateOf("Technical (AI & Java)") // HR (Behavioral), Technical (AI & Java)
    val interviewQuestionIndex = mutableStateOf(0)
    val interviewAnswerText = mutableStateOf("")
    val isInterviewLoading = mutableStateOf(false)
    val interviewFeedback = mutableStateOf<String?>(null)

    // Admin metrics
    val adminModeEnabled = mutableStateOf(false)
    val adminActiveLearners = mutableStateOf(142)
    val adminPlacementsThisMonth = mutableStateOf(38)

    // Preloaded Interview Questions
    private val technicalQuestions = listOf(
        "Explain the core differences between REST APIs and SOAP services. When would you use REST?",
        "What are index types in SQL, and how do they speed up database search queries?",
        "Describe what a Retrieval Augmented Generation (RAG) system is and why we need it in LLM pipelines.",
        "What is Apex programming, and how does it execute triggers inside Salesforce databases?"
    )

    private val hrQuestions = listOf(
        "Tell me about a time you worked in a team on tight timelines to complete a technical project. How did you coordinate?",
        "Why do you want to upskill at Brolly Academy, and what are your long-term career aspirations?",
        "How do you handle professional failure or constructive feedback from a trainer or team lead?",
        "What are your core package expectations and availability schedules for joining immediately?"
    )

    fun getCurrentInterviewQuestion(): String {
        val qList = if (interviewType.value.contains("Technical")) technicalQuestions else hrQuestions
        val idx = interviewQuestionIndex.value % qList.size
        return qList[idx]
    }

    init {
        // Initialize sample conversations if empty
        viewModelScope.launch {
            chatDao.getMessagesForSession("mentor").collect { list ->
                if (list.isEmpty()) {
                    chatDao.insertMessage(
                        ChatMessage(
                            sessionId = "mentor",
                            senderName = "Brolly AI",
                            content = "Hello! I am your Brolly Career & Tech AI Mentor. Ask me coding doubts, ask for custom career roadmaps, or evaluate technical interview systems in Hyderabad! How can I accelerate your learning today?",
                            isUser = false
                        )
                    )
                }
            }
        }
    }

    // --- Database actions with coroutines ---

    fun enrollInCourse(course: Course) {
        viewModelScope.launch {
            val existing = progressDao.getProgressForCourse(course.id)
            if (existing == null) {
                progressDao.insertOrUpdateProgress(
                    CourseProgress(
                        courseId = course.id,
                        title = course.title,
                        category = course.category,
                        enrolled = true,
                        progressPercent = 0,
                        completedLessons = ""
                    )
                )
            }
        }
    }

    fun completeLesson(courseId: String, lessonIndex: Int) {
        viewModelScope.launch {
            val progress = progressDao.getProgressForCourse(courseId)
            if (progress != null) {
                val completedList = if (progress.completedLessons.isEmpty()) {
                    setOf(lessonIndex)
                } else {
                    progress.completedLessons.split(",").mapNotNull { it.toIntOrNull() }.toSet() + lessonIndex
                }

                val totalLessons = BrollyData.CoursesList.find { it.id == courseId }?.lessons?.size ?: 4
                val percent = ((completedList.size.toFloat() / totalLessons.toFloat()) * 100).toInt().coerceIn(0, 100)
                val completedString = completedList.joinToString(",")

                progressDao.insertOrUpdateProgress(
                    progress.copy(
                        progressPercent = percent,
                        completedLessons = completedString
                    )
                )
            }
        }
    }

    // --- Gemini Interactive Calls ---

    fun sendMentorMessage(userMsg: String) {
        if (userMsg.trim().isEmpty()) return

        viewModelScope.launch {
            // Save user message to Room
            chatDao.insertMessage(
                ChatMessage(
                    sessionId = "mentor",
                    senderName = "Sai Kumar (You)",
                    content = userMsg,
                    isUser = true
                )
            )

            isMentorLoading.value = true

            val systemPrompt = """
                You are the Brolly Academy Career & Tech AI Mentor. Brolly Academy is Hyderabad's topmost IT training platform, offering online and physical hybrid classes in Madhapur and Ameerpet. Keep your tone encouraging, professional, and knowledgeable.
                Help students build roadmaps, solve code errors, and understand local Hyderabad job prospects (like Hitec city, Gachibowli, Ameerpet, Madhapur). Deliver answers in structured bullet points, using bold letters for focus areas. Use proper code blocks with markdown for code snippets.
            """.trimIndent()

            val response = geminiRepository.generateAiResponse(userMsg, systemPrompt)

            // Save AI reply to Room
            chatDao.insertMessage(
                ChatMessage(
                    sessionId = "mentor",
                    senderName = "Brolly AI Mentor",
                    content = response,
                    isUser = false
                )
            )

            isMentorLoading.value = false
        }
    }

    fun clearMentorChat() {
        viewModelScope.launch {
            chatDao.clearSession("mentor")
            chatDao.insertMessage(
                ChatMessage(
                    sessionId = "mentor",
                    senderName = "Brolly AI",
                    content = "Hello! I am your Brolly Career & Tech AI Mentor. Ask me coding doubts, ask for custom career roadmaps, or evaluate technical interview systems in Hyderabad! How can I accelerate your learning today?",
                    isUser = false
                )
            )
        }
    }

    fun sendCourseDoubtMessage(courseId: String, userMsg: String) {
        if (userMsg.trim().isEmpty()) return

        viewModelScope.launch {
            val sessionId = "course_$courseId"

            // Save user doubt to Room
            chatDao.insertMessage(
                ChatMessage(
                    sessionId = sessionId,
                    senderName = "Sai Kumar (You)",
                    content = userMsg,
                    isUser = true
                )
            )

            isCourseDoubtLoading.value = true

            val systemPrompt = """
                You are Brolly Academy's expert LMS Assistant for the course '$courseId'. Keep your answers brief, informative, and focused on helping the student solve their exact doubt regarding the syllabus. Explain concept simply and provide beautiful clean code examples with comments.
            """.trimIndent()

            val response = geminiRepository.generateAiResponse(userMsg, systemPrompt)

            // Save AI feedback to Room
            chatDao.insertMessage(
                ChatMessage(
                    sessionId = sessionId,
                    senderName = "Course Assistant",
                    content = response,
                    isUser = false
                )
            )

            isCourseDoubtLoading.value = false
        }
    }

    fun getCourseDoubtsFlow(courseId: String): StateFlow<List<ChatMessage>> {
        val sessionId = "course_$courseId"
        return chatDao.getMessagesForSession(sessionId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf(
                    ChatMessage(
                        sessionId = sessionId,
                        senderName = "Course Assistant",
                        content = "Need help with this video or the exercises? Ask me any doubts here and I will instantly help you solve the problem!",
                        isUser = false
                    )
                )
            )
    }

    fun clearCourseDoubts(courseId: String) {
        viewModelScope.launch {
            val sessionId = "course_$courseId"
            chatDao.clearSession(sessionId)
        }
    }

    // --- ATS Resume Reviewer ---

    fun analyzeResumeText() {
        val text = resumeTextState.value
        if (text.trim().isEmpty()) return

        viewModelScope.launch {
            isAtsScoring.value = true
            val prompt = """
                Evaluate this student resume and calculate an ATS Compatibility Score out of 100.
                Provide clear, visual feedback structured exactly under these four headings:
                1. 😂 ATS Score (e.g. 72/100)
                2. 🔍 Missing Key Tech Words (suggest additions for Full Stack, AI, Web or Salesforce depending on resume content)
                3. 🛠️ Formatting & Structure Corrections
                4. 📈 Brolly Recommended Projects to increase placement selection rates in Hyderabad.
                
                Resume content:
                $text
            """.trimIndent()

            val systemPrompt = "You are Brolly Academy's automated Career Services ATS Checker. Analyze resumes for technical alignment, readability, and Hyderabad corporate hiring suitability."

            val result = geminiRepository.generateAiResponse(prompt, systemPrompt)
            atsScoreResult.value = result
            isAtsScoring.value = false
        }
    }

    // --- Live Mock Interview Feedbacks ---

    fun submitInterviewAnswer() {
        val answer = interviewAnswerText.value
        if (answer.trim().isEmpty()) return

        viewModelScope.launch {
            isInterviewLoading.value = true
            val currentQuestion = getCurrentInterviewQuestion()

            val prompt = """
                The candidate is answering this specific mock interview question:
                Question: $currentQuestion
                Candidate's Answer: $answer
                
                Provide a visual evaluation report summarizing:
                1. ⭐️ Answer Rating (Good, Average, Poor)
                2. 🎯 Technical Accuracy & Completeness
                3. 🚀 Improved Sample Answer (provide the ideal industry-standard answer they should remember)
                4. 💡 Quick prep tip for Hyderabad recruiters.
            """.trimIndent()

            val systemPrompt = "You are Brolly Academy's senior mock interviewer panel. Review candidate answers objectively and deliver educational prep feedback."

            val result = geminiRepository.generateAiResponse(prompt, systemPrompt)
            interviewFeedback.value = result
            isInterviewLoading.value = false
        }
    }

    fun nextInterviewQuestion() {
        interviewQuestionIndex.value += 1
        interviewAnswerText.value = ""
        interviewFeedback.value = null
    }

    fun resetInterview() {
        interviewQuestionIndex.value = 0
        interviewAnswerText.value = ""
        interviewFeedback.value = null
    }

    // --- Saved Jobs management ---

    fun saveJob(job: JobOpening, initialStatus: String = "Bookmarked") {
        viewModelScope.launch {
            jobDao.insertOrUpdateJob(
                SavedJob(
                    jobId = job.id,
                    title = job.title,
                    company = job.company,
                    location = job.location,
                    type = job.type,
                    salary = job.salary,
                    status = initialStatus
                )
            )
        }
    }

    fun updateJobStatus(jobId: String, status: String) {
        viewModelScope.launch {
            jobDao.updateJobStatus(jobId, status)
        }
    }

    fun removeSavedJob(jobId: String) {
        viewModelScope.launch {
            val list = savedJobs.value
            val item = list.find { it.jobId == jobId }
            if (item != null) {
                jobDao.deleteJob(item)
            }
        }
    }
}
