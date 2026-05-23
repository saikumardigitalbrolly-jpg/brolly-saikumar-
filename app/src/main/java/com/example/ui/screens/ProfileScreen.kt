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
import com.example.ui.viewmodel.BrollyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: BrollyViewModel,
    modifier: Modifier = Modifier
) {
    var adminEnabled by viewModel.adminModeEnabled
    var showCertificateDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF05060B))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Top Identity Avatar Card ---
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF8A2BE2), Color(0xFF2E5BFF))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SK",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    }

                    Column {
                        Text(
                            text = "Sai Kumar (You)",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "saikumardigitalbrolly@gmail.com",
                            fontSize = 11.sp,
                            color = Color(0xFFA7A9BE)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFF8906).copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Brolly Hyderabad Hybrid Cohort",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF8906)
                            )
                        }
                    }
                }
            }
        }

        // --- 2. Developer & Trainer Admin panel switch ---
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)), // Sleek charcoal backdrop
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00FFC2).copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Switch",
                            tint = Color(0xFF00FFC2),
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = "Brolly Trainer / Admin Hub",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Manage courses, tracking dashboard metrics",
                                fontSize = 10.sp,
                                color = Color(0xFFA7A9BE)
                            )
                        }
                    }

                    Switch(
                        checked = adminEnabled,
                        onCheckedChange = { adminEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF00FFC2),
                            checkedTrackColor = Color(0xFF00FFC2).copy(alpha = 0.3f),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.testTag("admin_toggle_switch")
                    )
                }
            }
        }

        // --- 3. Dynamic Sub-views: Standard Student accomplishments vs Admin dashboard ---
        if (adminEnabled) {
            item { AdminDashboardSection(viewModel) }
        } else {
            // Standard Student accomplishments view
            item {
                Text(
                    text = "LMS Certifications",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCertificateDialog = true }
                        .testTag("certificate_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFD700).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardMembership,
                                contentDescription = "Certificate Icon",
                                tint = Color(0xFFFFD700)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Brolly Certification Course Completion",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Click to preview and share completion credentials.",
                                fontSize = 11.sp,
                                color = Color(0xFFA7A9BE)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open Certificate",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Streak/Gamification Leaderboard
            item {
                Text(
                    text = "Brolly Global Leaderboard (Hyderabad)",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Leaderboard,
                                    contentDescription = "Leaderboard",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Monthly XP Competition",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "Top 10 Hired Direct",
                                fontSize = 10.sp,
                                color = Color(0xFF4CDC9F),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        BrollyData.Leaderboard.forEach { student ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (student.isCurrentUser) Color(0xFF2E5BFF).copy(alpha = 0.12f)
                                        else Color.Transparent
                                    )
                                    .padding(vertical = 4.dp, horizontal = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "#${student.rank}",
                                        fontWeight = FontWeight.Bold,
                                        color = if (student.rank <= 3) Color(0xFFFFD700) else Color(0xFFA7A9BE),
                                        fontSize = 12.sp,
                                        modifier = Modifier.width(24.dp)
                                    )
                                    Text(
                                        text = student.name,
                                        fontWeight = if (student.isCurrentUser) FontWeight.Bold else FontWeight.Normal,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = "${student.xp} XP",
                                    color = if (student.isCurrentUser) Color(0xFF2E5BFF) else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Certificate Dialog
    if (showCertificateDialog) {
        AlertDialog(
            onDismissRequest = { showCertificateDialog = false },
            confirmButton = {
                Button(onClick = { showCertificateDialog = false }) {
                    Text("Download PDF")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCertificateDialog = false }) {
                    Text("Close")
                }
            },
            title = {
                Text("Certificate of Outstanding Achievement", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF11121A))
                        .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(8.dp))
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("BROLLY TECH ACADEMY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E5BFF))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("This is to certify that", fontSize = 10.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("SAI KUMAR", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("has successfully mastered professional training in", fontSize = 10.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Generative AI & Machine Learning Masters", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Hyderabad Cohort • Grade: Outstanding / A+", fontSize = 9.sp, color = Color.Gray)
                }
            }
        )
    }
}

// --- Admin hub layout ---
@Composable
fun AdminDashboardSection(viewModel: BrollyViewModel) {
    val progressList by viewModel.allProgress.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Brolly HQ Analytical Dashboard",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00FFC2),
            fontSize = 15.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdminMetricCard("Total Active Learners", "142", "👥", Modifier.weight(1f))
            AdminMetricCard("Placements This Month", "38", "📈", Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdminMetricCard("LMS Average Score", "84%", "📚", Modifier.weight(1f))
            AdminMetricCard("Hyderabad Classrooms", "4 Batches", "🏫", Modifier.weight(1f))
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Trainer Classroom Allocation",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                AllocationRow("Dr. Srikanth M.", "Generative AI Masters (Madhapur)", "10 Enrolled")
                AllocationRow("K. Rajesh", "Salesforce Admin (Ameerpet)", "15 Enrolled")
                AllocationRow("Anish Kumar", "Full Stack Java (Hybrid)", "24 Enrolled")
                AllocationRow("Meera Nair", "Data Science (Madhapur Classroom)", "12 Enrolled")
            }
        }
    }
}

@Composable
fun AdminMetricCard(title: String, score: String, iconStr: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(85.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11121A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title, fontSize = 10.sp, color = Color(0xFFA7A9BE), maxLines = 1)
                Text(iconStr, fontSize = 11.sp)
            }
            Text(score, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun AllocationRow(trainer: String, cohort: String, slots: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(trainer, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
            Text(cohort, fontSize = 10.sp, color = Color(0xFFA7A9BE))
        }
        Text(slots, fontSize = 11.sp, color = Color(0xFF00FFC2), fontWeight = FontWeight.Bold)
    }
}
