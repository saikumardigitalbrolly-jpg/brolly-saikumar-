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
import com.example.data.BrollyData
import com.example.data.JobOpening
import com.example.ui.viewmodel.BrollyViewModel

@Composable
fun JobsScreen(
    viewModel: BrollyViewModel,
    modifier: Modifier = Modifier
) {
    val savedPositions by viewModel.savedJobs.collectAsState()
    var expandedJobId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF05060B))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Career Portal Header ---
        item {
            Column {
                Text(
                    text = "Brolly Career & Placements",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                )
                Text(
                    text = "Track your corporate placements and apply directly to vacancies.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFA7A9BE)
                    )
                )
            }
        }

        // --- 2. Live Placement Pipeline Tracker Tracker ---
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Your Application Status Tracker",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PipelineStep(
                            label = "Applied",
                            activeCount = savedPositions.filter { it.status == "Applied" }.size,
                            color = Color(0xFF2E5BFF)
                        )
                        PipelineArrow()
                        PipelineStep(
                            label = "Tech Round",
                            activeCount = savedPositions.filter { it.status == "Tech Round" }.size,
                            color = Color(0xFF8A2BE2)
                        )
                        PipelineArrow()
                        PipelineStep(
                            label = "Hired",
                            activeCount = savedPositions.filter { it.status == "Offer" }.size,
                            color = Color(0xFF4CDC9F)
                        )
                    }
                }
            }
        }

        // --- 3. Saved Jobs Quick Board ---
        if (savedPositions.isNotEmpty()) {
            item {
                Text(
                    text = "My Bookmarks & Applications",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    savedPositions.forEach { saved ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = saved.company,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E5BFF)
                                    )
                                    Text(
                                        text = saved.title,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Text(
                                            text = "Status: ${saved.status}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = when (saved.status) {
                                                "Offer" -> Color(0xFF4CDC9F)
                                                "Tech Round" -> Color(0xFF8A2BE2)
                                                else -> Color(0xFFFF8906)
                                            }
                                        )
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            // Cycle status applied -> tech -> offer
                                            val nextStatus = when (saved.status) {
                                                "Bookmarked" -> "Applied"
                                                "Applied" -> "Tech Round"
                                                "Tech Round" -> "Offer"
                                                else -> "Bookmarked"
                                            }
                                            viewModel.updateJobStatus(saved.jobId, nextStatus)
                                        },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFF05060B))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Cached,
                                            contentDescription = "Update Status cycle",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.removeSavedJob(saved.jobId) },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFEF4565).copy(alpha = 0.1f))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Saved",
                                            tint = Color(0xFFEF4565),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 4. Live Open Active Vacancies Board ---
        item {
            Text(
                text = "Hyderabad Live Vacancies",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 15.sp
            )
        }

        items(BrollyData.JobOpenings) { job ->
            val isExpanded = expandedJobId == job.id
            val isBookmarked = savedPositions.any { it.jobId == job.id }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedJobId = if (isExpanded) null else job.id }
                    .testTag("vacancy_card_${job.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = job.company,
                                color = Color(0xFF2E5BFF),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = job.title,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }

                        IconButton(
                            onClick = {
                                if (isBookmarked) {
                                    viewModel.removeSavedJob(job.id)
                                } else {
                                    viewModel.saveJob(job, "Bookmarked")
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save Position Toggle",
                                tint = if (isBookmarked) Color(0xFFFF8906) else Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "📍 ${job.location}",
                            fontSize = 11.sp,
                            color = Color(0xFFA7A9BE)
                        )
                        Text(
                            text = "💰 ${job.salary}",
                            fontSize = 11.sp,
                            color = Color(0xFFA7A9BE)
                        )
                        Text(
                            text = "⏱️ ${job.type}",
                            fontSize = 11.sp,
                            color = Color(0xFFA7A9BE)
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Divider(color = Color(0xFF33333C))

                            Text(
                                text = "Job Overview",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Text(
                                text = job.description,
                                fontSize = 12.sp,
                                color = Color(0xFFA7A9BE),
                                lineHeight = 16.sp
                            )

                            Text(
                                text = "Required Core Skills",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                job.skillsNeeded.forEach { skill ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFF2E5BFF).copy(alpha = 0.15f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(skill, fontSize = 10.sp, color = Color.White)
                                    }
                                }
                            }

                            Text(
                                text = "Key Requirements & Eligibility",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            job.requirements.forEach { rule ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("•", color = Color(0xFF2E5BFF), fontSize = 12.sp)
                                    Text(
                                        text = rule,
                                        fontSize = 12.sp,
                                        color = Color(0xFFA7A9BE),
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = {
                                    viewModel.saveJob(job, "Applied")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CDC9F)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("apply_vacancy_btn_${job.id}")
                            ) {
                                Text(
                                    text = "Send Direct Application via Brolly LMS",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PipelineStep(label: String, activeCount: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (activeCount > 0) color else Color(0xFF05060B)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = activeCount.toString(),
                fontWeight = FontWeight.Bold,
                color = if (activeCount > 0) Color.White else Color(0xFFA7A9BE),
                fontSize = 14.sp
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (activeCount > 0) color else Color(0xFFA7A9BE)
        )
    }
}

@Composable
fun PipelineArrow() {
    Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = "Next",
        tint = Color.Gray,
        modifier = Modifier.size(16.dp)
    )
}
