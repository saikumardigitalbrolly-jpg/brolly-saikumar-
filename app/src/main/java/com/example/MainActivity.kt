package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.MentorScreen
import com.example.ui.screens.JobsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.BrollyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: BrollyViewModel = viewModel()
                MainAppShell(viewModel)
            }
        }
    }
}

@Composable
fun MainAppShell(viewModel: BrollyViewModel) {
    val activeTab by viewModel.activeTab

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .testTag("app_bottom_nav_bar")
                    .windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = Color(0xFF0A0A14), // Sleek pitch slate bottom nav bar background
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = activeTab == "home",
                    onClick = { viewModel.activeTab.value = "home" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home tab icon") },
                    label = { Text("Home", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_home"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color(0xFF64748B),
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color(0xFF64748B),
                        indicatorColor = Color(0xFF2E5BFF)
                    )
                )

                NavigationBarItem(
                    selected = activeTab == "learn",
                    onClick = { viewModel.activeTab.value = "learn" },
                    icon = { Icon(Icons.Default.School, contentDescription = "LMS Courses tab icon") },
                    label = { Text("Learn", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_learn"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color(0xFF64748B),
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color(0xFF64748B),
                        indicatorColor = Color(0xFF2E5BFF)
                    )
                )

                NavigationBarItem(
                    selected = activeTab == "mentor",
                    onClick = { viewModel.activeTab.value = "mentor" },
                    icon = { Icon(Icons.Default.Psychology, contentDescription = "AI Career Mentor tab icon") },
                    label = { Text("AI Mentor", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_mentor"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color(0xFF64748B),
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color(0xFF64748B),
                        indicatorColor = Color(0xFF2E5BFF)
                    )
                )

                NavigationBarItem(
                    selected = activeTab == "jobs",
                    onClick = { viewModel.activeTab.value = "jobs" },
                    icon = { Icon(Icons.Default.Work, contentDescription = "Jobs Placements tab icon") },
                    label = { Text("Jobs", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_jobs"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color(0xFF64748B),
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color(0xFF64748B),
                        indicatorColor = Color(0xFF2E5BFF)
                    )
                )

                NavigationBarItem(
                    selected = activeTab == "profile",
                    onClick = { viewModel.activeTab.value = "profile" },
                    icon = { Icon(Icons.Default.AccountBox, contentDescription = "Student Profile tab icon") },
                    label = { Text("Profile", fontSize = 10.sp) },
                    modifier = Modifier.testTag("nav_item_profile"),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color(0xFF64748B),
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color(0xFF64748B),
                        indicatorColor = Color(0xFF2E5BFF)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF05060B)) // Sleek dark pitch background
                .padding(innerPadding)
        ) {
            when (activeTab) {
                "home" -> HomeScreen(viewModel)
                "learn" -> LearnScreen(viewModel)
                "mentor" -> MentorScreen(viewModel)
                "jobs" -> JobsScreen(viewModel)
                "profile" -> ProfileScreen(viewModel)
                else -> HomeScreen(viewModel)
            }
        }
    }
}
