package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.BrollyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorScreen(
    viewModel: BrollyViewModel,
    modifier: Modifier = Modifier
) {
    // Top Tabs: 0 -> AI Chat, 1 -> ATS Resume review, 2 -> Mock Interview Practice
    var activeMentorTab by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF05060B))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Core Header
        Column {
            Text(
                text = "Brolly AI Career Mentor",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 24.sp
                )
            )
            Text(
                text = "Learn smarter. Review resumes. Dominate interviews.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFA7A9BE)
                )
            )
        }

        // Horizontal Segmented Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF11121A)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val tabs = listOf("AI Assistant", "ATS Auditor", "Mock Interviews")
            tabs.forEachIndexed { idx, tabTitle ->
                val active = activeMentorTab == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeMentorTab = idx }
                        .background(if (active) Color(0xFF2E5BFF) else Color.Transparent)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabTitle,
                        color = if (active) Color.White else Color(0xFFA7A9BE),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Subsystems Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (activeMentorTab) {
                0 -> MentorChatSubsystem(viewModel)
                1 -> AtsResumeSubsystem(viewModel)
                2 -> MockInterviewSubsystem(viewModel)
            }
        }
    }
}

// --- Tab 1: AI Chat Companion ---
@Composable
fun MentorChatSubsystem(viewModel: BrollyViewModel) {
    val messages by viewModel.mentorChatMessages.collectAsState()
    val isMentorLoading by viewModel.isMentorLoading
    var queryText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Discussion & Roadmaps",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFA7A9BE)
            )
            Text(
                text = "Reset Conversation",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4565),
                modifier = Modifier.clickable { viewModel.clearMentorChat() }
            )
        }

        // Conversational Scroll
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF05060B))
                .clip(RoundedCornerShape(12.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                val isUsr = msg.isUser
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUsr) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUsr) Color(0xFF2E5BFF) else Color(0xFF11121A)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (isUsr) "You" else "Brolly AI Mentor 🤖",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUsr) Color(0xFFD0BCFF) else Color(0xFF8A2BE2)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = msg.content,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(visible = isMentorLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Color(0xFF2E5BFF),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Brolly AI is typing standard roadmap answers...",
                    fontSize = 11.sp,
                    color = Color(0xFF4CDC9F)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input Layout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = queryText,
                onValueChange = { queryText = it },
                placeholder = { Text("Ask coding doubts or roadmap rules...", fs = 13.sp) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF11121A),
                    unfocusedContainerColor = Color(0xFF11121A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("mentor_query_input")
            )

            Button(
                onClick = {
                    viewModel.sendMentorMessage(queryText)
                    queryText = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5BFF)),
                modifier = Modifier
                    .height(52.dp)
                    .testTag("mentor_query_send"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Prompt",
                    tint = Color.White
                )
            }
        }
    }
}

private fun Text(text: String, fs: androidx.compose.ui.unit.TextUnit) {
    // Helper helper constructor
}

// --- Tab 2: ATS Resume review ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtsResumeSubsystem(viewModel: BrollyViewModel) {
    val isScoring by viewModel.isAtsScoring
    val atsResult by viewModel.atsScoreResult
    var rawText by viewModel.resumeTextState

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "ATS Scanner & Auditor",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = "Paste your CV or list of technical skills/experience below to test compatibility.",
                fontSize = 11.sp,
                color = Color(0xFFA7A9BE)
            )
        }

        item {
            OutlinedTextField(
                value = rawText,
                onValueChange = { rawText = it },
                label = { Text("Paste Resume Content here...", color = Color(0xFFA7A9BE)) },
                minLines = 4,
                maxLines = 8,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2E5BFF),
                    unfocusedBorderColor = Color(0xFF11121A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("resume_input_field")
            )
        }

        item {
            Button(
                onClick = { viewModel.analyzeResumeText() },
                enabled = rawText.trim().isNotEmpty() && !isScoring,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5BFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("analyze_resume_btn")
            ) {
                if (isScoring) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Text("Evaluate Compatibility", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (atsResult != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "AI Feedback Analysis",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CDC9F),
                                fontSize = 14.sp
                            )
                            IconButton(onClick = { viewModel.atsScoreResult.value = null }) {
                                Icon(Icons.Default.Close, contentDescription = "Close Feedback", tint = Color.Gray)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = atsResult!!,
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

// --- Tab 3: Mock Interview Simulator ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockInterviewSubsystem(viewModel: BrollyViewModel) {
    val interviewType by viewModel.interviewType
    val currentIdx by viewModel.interviewQuestionIndex
    val currentQuestion = viewModel.getCurrentInterviewQuestion()
    var ansText by viewModel.interviewAnswerText
    val isInterviewLoading by viewModel.isInterviewLoading
    val feedback by viewModel.interviewFeedback

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Brolly AI Mock Interview Panel",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = "Answer the question and receive dynamic suggestions for technical accuracy.",
                fontSize = 11.sp,
                color = Color(0xFFA7A9BE)
            )
        }

        // Dropdown Selector simple representations
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF11121A))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val modes = listOf("Technical (AI & Java)", "HR (Behavioral)")
                modes.forEach { modeName ->
                    val active = interviewType == modeName
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (active) Color(0xFF8A2BE2) else Color.Transparent)
                            .clickable {
                                viewModel.interviewType.value = modeName
                                viewModel.resetInterview()
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = modeName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Active Question Deck
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E5BFF).copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "QUESTION ${currentIdx + 1}",
                        color = Color(0xFF2E5BFF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentQuestion,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // User Input Answer Box
        item {
            OutlinedTextField(
                value = ansText,
                onValueChange = { ansText = it },
                label = { Text("Type your comprehensive answer here...", color = Color(0xFFA7A9BE)) },
                minLines = 3,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2E5BFF),
                    unfocusedBorderColor = Color(0xFF11121A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("interview_answer_input")
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.submitInterviewAnswer() },
                    enabled = ansText.trim().isNotEmpty() && !isInterviewLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5BFF)),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(44.dp)
                        .testTag("submit_interview_btn")
                ) {
                    if (isInterviewLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                    } else {
                        Text("Get Evaluation", fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.nextInterviewQuestion() },
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("next_interview_btn")
                ) {
                    Text("Skip / Next")
                }
            }
        }

        if (feedback != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Panel Review",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CDC9F),
                                fontSize = 14.sp
                            )
                            IconButton(onClick = { viewModel.interviewFeedback.value = null }) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = feedback!!,
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
