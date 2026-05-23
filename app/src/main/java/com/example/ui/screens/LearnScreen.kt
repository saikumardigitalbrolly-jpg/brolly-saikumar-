package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BrollyData
import com.example.data.Course
import com.example.data.Lesson
import com.example.ui.viewmodel.BrollyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    viewModel: BrollyViewModel,
    modifier: Modifier = Modifier
) {
    val activePlayingCourse by viewModel.activePlayingCourse
    val progressList by viewModel.allProgress.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF05060B))
    ) {
        if (activePlayingCourse == null) {
            // Screen A: Show Course Catalog
            CourseCatalogView(viewModel)
        } else {
            // Screen B: Immersive LMS Video & Learning Interface
            LmsLearingInterface(viewModel, activePlayingCourse!!)
        }
    }
}

@Composable
fun CourseCatalogView(viewModel: BrollyViewModel) {
    val categories = listOf("All", "AI Courses", "Full Stack", "Salesforce", "DevOps", "Data Science")
    var selectedCategory by remember { mutableStateOf("All") }

    val progressList by viewModel.allProgress.collectAsState()

    val filteredCourses = if (selectedCategory == "All") {
        BrollyData.CoursesList
    } else {
        BrollyData.CoursesList.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Brolly Tech Academy LMS",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 24.sp
                )
            )
            Text(
                text = "Choose your upskilling goal and attend live interactive classes.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFA7A9BE)
                )
            )
        }

        // Horizontal Category Row
        item {
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
                edgePadding = 0.dp,
                divider = {},
                containerColor = Color.Transparent,
                indicator = {}
            ) {
                categories.forEach { category ->
                    val isSelected = category == selectedCategory
                    Tab(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Color(0xFF2E5BFF) else Color(0xFF11121A))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        text = {
                            Text(
                                text = category,
                                color = if (isSelected) Color.White else Color(0xFFA7A9BE),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }
        }

        items(filteredCourses) { course ->
            val progress = progressList.find { it.courseId == course.id }
            val isEnrolled = progress?.enrolled == true

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.enrollInCourse(course)
                        viewModel.activePlayingCourse.value = course
                        // Find first incomplete lesson or set default 0
                        val completedSet = progress?.completedLessons?.split(",")?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
                        val nextLessonIdx = (0 until course.lessons.size).find { !completedSet.contains(it) } ?: 0
                        viewModel.activePlayingLessonIndex.value = nextLessonIdx
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = course.category.uppercase(),
                        color = Color(0xFF8A2BE2),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = course.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = course.description,
                        fontSize = 12.sp,
                        color = Color(0xFFA7A9BE),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Trainer: ${course.trainer}",
                                fontSize = 11.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${course.duration} | ${course.mode}",
                                fontSize = 10.sp,
                                color = Color(0xFFA7A9BE)
                            )
                        }

                        if (isEnrolled) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF8A2BE2).copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Resume (${progress.progressPercent}%)",
                                    color = Color(0xFFD0BCFF),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    viewModel.enrollInCourse(course)
                                    viewModel.activePlayingCourse.value = course
                                    viewModel.activePlayingLessonIndex.value = 0
                                },
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5BFF)),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Enroll Now", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LmsLearingInterface(
    viewModel: BrollyViewModel,
    course: Course
) {
    val activeLessonIndex by viewModel.activePlayingLessonIndex
    val lesson = course.lessons.getOrElse(activeLessonIndex) { course.lessons.first() }

    // LMS Tabs: 0 -> Curriculum, 1 -> AI Doubt Assistant, 2 -> Resource Downloads
    var activeLmsTab by remember { mutableStateOf(0) }

    // Simulated Playing Variable
    var isSimulatedPlaying by remember { mutableStateOf(false) }
    var simulatedProgressPercent by remember { mutableStateOf(0.42f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Back Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.activePlayingCourse.value = null },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF11121A))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to list",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = course.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Text(
                    text = "Instructor: ${course.trainer}",
                    fontSize = 11.sp,
                    color = Color(0xFFA7A9BE)
                )
            }
        }

        // --- 1. Elevated Dynamic Video Player Deck ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF12111E), Color(0xFF1C1A2F))
                        )
                    )
            ) {
                // Background artistic lines or placeholder
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Lesson ${lesson.index + 1}: ${lesson.title}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    // Large central simulate button
                    IconButton(
                        onClick = { isSimulatedPlaying = !isSimulatedPlaying },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFF2E5BFF))
                    ) {
                        Icon(
                            imageVector = if (isSimulatedPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Simulated play indicator",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Playing seekbar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isSimulatedPlaying) "07:15" else "00:00",
                            color = Color(0xFFA7A9BE),
                            fontSize = 10.sp
                        )
                        Slider(
                            value = simulatedProgressPercent,
                            onValueChange = { simulatedProgressPercent = it },
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF2E5BFF),
                                activeTrackColor = Color(0xFF2E5BFF),
                                inactiveTrackColor = Color(0xFF33333C)
                            )
                        )
                        Text(
                            text = lesson.duration,
                            color = Color(0xFFA7A9BE),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Auto lesson solver helper row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Currently Watching",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFA7A9BE)
            )

            Button(
                onClick = {
                    viewModel.completeLesson(course.id, lesson.index)
                    // Auto advance if next lesson exists
                    if (lesson.index < course.lessons.size - 1) {
                        viewModel.activePlayingLessonIndex.value = lesson.index + 1
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CDC9F)),
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check Complete",
                        modifier = Modifier.size(14.dp),
                        tint = Color.Black
                    )
                    Text("Completed", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Modern Tab Pickers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF11121A)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val tabs = listOf("Curriculum", "AI doubts Bot", "Project Files")
            tabs.forEachIndexed { idx, tabTitle ->
                val active = activeLmsTab == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeLmsTab = idx }
                        .background(if (active) Color(0xFF2E5BFF) else Color.Transparent)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabTitle,
                        color = if (active) Color.White else Color(0xFFA7A9BE),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- Tab Views ---
        when (activeLmsTab) {
            0 -> CurriculumTab(course, activeLessonIndex) { selectedIdx ->
                viewModel.activePlayingLessonIndex.value = selectedIdx
            }
            1 -> CourseDoubtBotTab(viewModel, course.id)
            2 -> ResourceFilesTab()
        }
    }
}

@Composable
fun CurriculumTab(
    course: Course,
    activeLessonIndex: Int,
    onLessonClick: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(course.lessons) { lesson ->
            val isActive = lesson.index == activeLessonIndex

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLessonClick(lesson.index) }
                    .testTag("curriculum_lesson_card_${lesson.index}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isActive) Color(0xFF2E5BFF).copy(alpha = 0.15f) else Color(0xFF11121A)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isActive) Color(0xFF2E5BFF).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.06f)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) Color(0xFF2E5BFF) else Color(0xFF05060B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (lesson.index + 1).toString(),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lesson.title,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            text = lesson.summary,
                            fontSize = 11.sp,
                            color = Color(0xFFA7A9BE),
                            maxLines = 1
                        )
                    }

                    Text(
                        text = lesson.duration,
                        fontSize = 11.sp,
                        color = Color(0xFFA7A9BE)
                    )
                }
            }
        }
    }
}

@Composable
fun CourseDoubtBotTab(viewModel: BrollyViewModel, courseId: String) {
    val doubtsMessages by viewModel.getCourseDoubtsFlow(courseId).collectAsState()
    val isDoubtLoading by viewModel.isCourseDoubtLoading

    var userQueryText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Real-time Course Doubt Resolver",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Clear Chat",
                        color = Color(0xFFEF4565),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.clearCourseDoubts(courseId) }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Doubt chat list limit size
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(doubtsMessages) { msg ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (msg.isUser) Color(0xFF2E5BFF) else Color(0xFF05060B))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .widthIn(max = 240.dp)
                                ) {
                                    Text(
                                        text = msg.content,
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(visible = isDoubtLoading) {
                    Text(
                        text = "🤔 Thinking...",
                        color = Color(0xFF4CDC9F),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Input field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = userQueryText,
                        onValueChange = { userQueryText = it },
                        placeholder = { Text("Ask your exact doubt here...", fontSize = 11.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF05060B),
                            unfocusedContainerColor = Color(0xFF05060B),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color(0xFF2E5BFF)
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(6.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.sendCourseDoubtMessage(courseId, userQueryText)
                            userQueryText = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5BFF)),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("course_doubt_submit"),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResourceFilesTab() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ResourceDownloadItem("📁 Lecture Handouts Framework Slide.pdf", "8.4 MB")
        ResourceDownloadItem("💻 Hands-on Exercises & Problem Sets.zip", "3.2 MB")
        ResourceDownloadItem("📄 Hyderabad Recommended Recruiter Checklist.txt", "12 KB")
    }
}

@Composable
fun ResourceDownloadItem(name: String, size: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                Text("Size: $size", fontSize = 10.sp, color = Color(0xFFA7A9BE))
            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download Resource",
                    tint = Color(0xFF2E5BFF)
                )
            }
        }
    }
}
