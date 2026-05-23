package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.LiveClass
import com.example.ui.viewmodel.BrollyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: BrollyViewModel,
    modifier: Modifier = Modifier
) {
    val progressList by viewModel.allProgress.collectAsState()
    val activeEnrollments = progressList.filter { it.enrolled }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF05060B)) // Sleek premium deep space background
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- 1. Top Greeting Header & Streak ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, Sai Kumar 👋",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 24.sp
                        )
                    )
                    Text(
                        text = "saikumardigitalbrolly@gmail.com",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFA7A9BE)
                        )
                    )
                }

                // XP Badges
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF11121A))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "XP Points Icon",
                        tint = Color(0xFFFFD700)
                    )
                    Text(
                        text = "1240 XP",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        // --- 2. Brolly Dynamic Skill Streak Widget ---
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("streak_widget_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFF8906)), // Flame orange
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Whatshot,
                                contentDescription = "Flame Icon",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "4-Day Daily Streak",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Keep upskilling to lock in Hyderabad placements!",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFA7A9BE)
                                )
                            )
                        }
                    }

                    Text(
                        text = "🔥 Ready",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF8906)
                        )
                    )
                }
            }
        }

        // --- 3. Welcome Hero Banner ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF2E5BFF), // Electric Blue
                                Color(0xFF8A2BE2)  // Vibrant Purple
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Learn Skills. Build Projects. Get Placed.",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "Hyderabad's elite hybrid tech ecosystem.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFE2E2E2)
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { viewModel.activeTab.value = "learn" }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Explore LMS Courses",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Explore arrow",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // --- 4. Resume Learning Section (Dynamic) ---
        if (activeEnrollments.isNotEmpty()) {
            item {
                Text(
                    text = "Continue Learning",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                activeEnrollments.forEach { progress ->
                    val fullCourse = BrollyData.CoursesList.find { it.id == progress.courseId }
                    if (fullCourse != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.activePlayingCourse.value = fullCourse
                                    // Set first uncompleted lesson
                                    val completedSet = progress.completedLessons.split(",")
                                        .mapNotNull { it.toIntOrNull() }.toSet()
                                    val nextLessonIdx = (0 until fullCourse.lessons.size).find { !completedSet.contains(it) } ?: 0
                                    viewModel.activePlayingLessonIndex.value = nextLessonIdx
                                    viewModel.activeTab.value = "learn"
                                }
                                .padding(vertical = 4.dp),
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
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = progress.category.uppercase(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E5BFF)
                                        )
                                        Text(
                                            text = progress.title,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 15.sp
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.PlayCircle,
                                        contentDescription = "Resume play button",
                                        tint = Color(0xFF2E5BFF),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    LinearProgressIndicator(
                                        progress = { progress.progressPercent / 100f },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = Color(0xFF8A2BE2),
                                        trackColor = Color(0xFF33333C)
                                    )
                                    Text(
                                        text = "${progress.progressPercent}% Completed",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 5. Upcoming Live Batches ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upcoming Live Classes",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "LMS Sync",
                    fontSize = 12.sp,
                    color = Color(0xFF2E5BFF),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { /* Reload */ }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(BrollyData.LiveClassesList) { liveClass ->
                    LiveClassCard(liveClass = liveClass)
                }
            }
        }

        // --- 6. Trending Industry Courses ---
        item {
            Text(
                text = "Trending Hybrid Programs",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(BrollyData.CoursesList) { course ->
                    TrendingCourseCard(course = course, onEnroll = {
                        viewModel.enrollInCourse(course)
                        viewModel.activePlayingCourse.value = course
                        viewModel.activePlayingLessonIndex.value = 0
                        viewModel.activeTab.value = "learn"
                    })
                }
            }
        }

        // --- 7. Hyderabad Campus Placement Updates Bulletin ---
        item {
            Text(
                text = "Hyderabad Career Hub",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlacementNoticeItem(
                    title = "Virtusa Hyderabad Campus Drive In-Progress",
                    timing = "2 hours ago",
                    desc = "Technical shortlisting has begun for Java Full Stack students. Update resumes!"
                )
                PlacementNoticeItem(
                    title = "Dr. Srikanth AI Agent Workshop Scheduled",
                    timing = "Yesterday",
                    desc = "Exclusive Madhapur Campus bootcamp on RAG and vector databases."
                )
            }
        }
    }
}

@Composable
fun LiveClassCard(liveClass: LiveClass) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(135.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (liveClass.isLive) "● LIVE NOW" else "SCHEDULED",
                        color = if (liveClass.isLive) Color(0xFFEF4565) else Color(0xFF2E5BFF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Trainer icon",
                            tint = Color(0xFFA7A9BE),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = liveClass.trainer,
                            fontSize = 10.sp,
                            color = Color(0xFFA7A9BE)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = liveClass.topic,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.sp,
                    maxLines = 1
                )
                Text(
                    text = liveClass.courseTitle,
                    fontSize = 10.sp,
                    color = Color(0xFFA7A9BE),
                    maxLines = 1
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = "Timer",
                        tint = Color(0xFFA7A9BE),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = liveClass.dateString,
                        fontSize = 10.sp,
                        color = Color(0xFFA7A9BE)
                    )
                }

                Button(
                    onClick = { /* Simulated join zoom link */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (liveClass.isLive) Color(0xFFEF4565) else Color(0xFF2E5BFF)
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text(
                        text = if (liveClass.isLive) "Join Session" else "Get Link",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TrendingCourseCard(
    course: Course,
    onEnroll: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = course.category.uppercase(),
                    color = Color(0xFF8A2BE2),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = course.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = course.duration,
                    fontSize = 11.sp,
                    color = Color(0xFFA7A9BE)
                )
                Text(
                    text = course.mode,
                    fontSize = 10.sp,
                    color = Color(0xFFA7A9BE)
                )
            }

            Button(
                onClick = onEnroll,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5BFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Enroll & Start", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PlacementNoticeItem(
    title: String,
    timing: String,
    desc: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.sp
                )
                Text(
                    text = timing,
                    fontSize = 10.sp,
                    color = Color(0xFFA7A9BE)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                fontSize = 11.sp,
                color = Color(0xFFA7A9BE)
            )
        }
    }
}
