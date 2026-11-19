package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.components.ProfileRoleSwitcherDialog
import com.example.ui.screens.admin.AdminContentManagementScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminStudentRosterScreen
import com.example.ui.screens.admin.CloudBackupScreen
import com.example.ui.screens.student.AssessmentReportScreen
import com.example.ui.screens.student.AssessmentScreen
import com.example.ui.screens.student.ProgressDashboardScreen
import com.example.ui.screens.student.StudentAppointmentsScreen
import com.example.ui.screens.student.StudentHomeScreen
import com.example.ui.theme.BoldBackground
import com.example.ui.theme.BoldMediumNeutral
import com.example.ui.theme.BoldNavBackground
import com.example.ui.theme.BoldOutline
import com.example.ui.theme.BoldPurple
import com.example.ui.theme.BoldPurpleContainer
import com.example.ui.theme.BoldPurpleDark
import com.example.ui.theme.BoldPurpleLight
import com.example.ui.viewmodel.CareerCounselingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerMainScreen(
    viewModel: CareerCounselingViewModel = viewModel()
) {
    val context = LocalContext.current

    // State from ViewModel
    val currentProfile by viewModel.currentProfile.collectAsState()
    val allProfiles by viewModel.allProfiles.collectAsState()
    val allCareers by viewModel.allCareers.collectAsState()
    val activeQuestions by viewModel.activeQuestions.collectAsState()
    val allQuestions by viewModel.allQuestions.collectAsState()
    val allResults by viewModel.allResults.collectAsState()
    val currentStudentResults by viewModel.currentStudentResults.collectAsState()
    val currentUserAppointments by viewModel.currentUserAppointments.collectAsState()
    val allDeadlines by viewModel.allDeadlines.collectAsState()
    val cloudSyncStatus by viewModel.cloudSyncStatus.collectAsState()
    val viewingResult by viewModel.viewingResult.collectAsState()

    // Test Taking State
    val testAnswers by viewModel.testAnswers.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val isSubmittingTest by viewModel.isSubmittingTest.collectAsState()

    // Roster filter states
    val rosterSearchQuery by viewModel.rosterSearchQuery.collectAsState()
    val rosterFilterLevel by viewModel.rosterFilterLevel.collectAsState()

    // Local UI states
    var selectedStudentTab by remember { mutableIntStateOf(0) } // 0: Home, 1: Test, 2: Progress, 3: Appointments
    var selectedAdminTab by remember { mutableIntStateOf(0) }   // 0: Dashboard, 1: Roster, 2: Content, 3: Appointments, 4: Cloud
    var showProfileSwitcher by remember { mutableStateOf(false) }

    val isFacultyAdmin = currentProfile?.role == UserRole.ADMIN

    // If actively viewing an assessment report detail
    if (viewingResult != null) {
        val activeResult = viewingResult!!
        val matchedCareers = viewModel.getMatchedCareersForResult(activeResult)

        AssessmentReportScreen(
            result = activeResult,
            matchedCareers = matchedCareers,
            currentUserRole = currentProfile?.role ?: UserRole.STUDENT,
            onDownloadPdf = {
                viewModel.exportAndSharePdf(context, activeResult)
            },
            onBookDebrief = {
                viewModel.setViewingResult(null)
                if (!isFacultyAdmin) {
                    selectedStudentTab = 3 // Go to appointments
                }
            },
            onSaveCounselorNotes = { notes ->
                viewModel.addCounselorNotes(activeResult.id, notes)
                Toast.makeText(context, "Counselor endorsement updated.", Toast.LENGTH_SHORT).show()
            },
            onBack = {
                viewModel.setViewingResult(null)
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showProfileSwitcher = true }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(BoldPurpleLight)
                                .border(2.dp, BoldPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val initials = currentProfile?.name
                                ?.split(" ")
                                ?.filter { it.isNotBlank() }
                                ?.mapNotNull { it.firstOrNull()?.toString() }
                                ?.take(2)
                                ?.joinToString("") ?: "ST"
                            Text(
                                text = initials.uppercase(),
                                color = BoldPurpleDark,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = if (isFacultyAdmin) "FACULTY PORTAL" else "CURRENT PROFILE",
                                style = MaterialTheme.typography.labelSmall,
                                color = BoldPurple,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.6.sp
                            )
                            Text(
                                text = (currentProfile?.name ?: "STUDENT.AI").uppercase(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp,
                                maxLines = 1
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = BoldPurpleContainer
                                ) {
                                    Text(
                                        text = if (isFacultyAdmin) "FACULTY ADMIN" else (currentProfile?.educationLevel?.displayName?.uppercase() ?: "STUDENT"),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = BoldPurpleDark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        letterSpacing = 0.8.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tap to switch role",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BoldMediumNeutral,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.performCloudSync() },
                        modifier = Modifier.testTag("btn_top_cloud_sync")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = "Cloud Storage Synced",
                            tint = Color(0xFF10B981)
                        )
                    }

                    IconButton(
                        onClick = { showProfileSwitcher = true },
                        modifier = Modifier.testTag("btn_top_switch_profile")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ManageAccounts,
                            contentDescription = "Switch Profile & Role",
                            tint = BoldPurple
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BoldBackground
                )
            )
        },
        bottomBar = {
            Surface(
                color = BoldNavBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BoldOutline)
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    val navItemColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BoldPurpleDark,
                        selectedTextColor = BoldPurpleDark,
                        indicatorColor = BoldPurpleContainer,
                        unselectedIconColor = BoldMediumNeutral,
                        unselectedTextColor = BoldMediumNeutral
                    )

                    if (!isFacultyAdmin) {
                        // Student Tabs
                        NavigationBarItem(
                            selected = selectedStudentTab == 0,
                            onClick = { selectedStudentTab = 0 },
                            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("HOME", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.8.sp) },
                            colors = navItemColors,
                            modifier = Modifier.testTag("tab_student_home")
                        )
                        NavigationBarItem(
                            selected = selectedStudentTab == 1,
                            onClick = {
                                viewModel.startNewAssessment()
                                selectedStudentTab = 1
                            },
                            icon = { Icon(imageVector = Icons.Default.Assignment, contentDescription = "Test") },
                            label = { Text("TEST", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.8.sp) },
                            colors = navItemColors,
                            modifier = Modifier.testTag("tab_student_test")
                        )
                        NavigationBarItem(
                            selected = selectedStudentTab == 2,
                            onClick = { selectedStudentTab = 2 },
                            icon = { Icon(imageVector = Icons.Default.ShowChart, contentDescription = "Progress") },
                            label = { Text("PROGRESS", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.8.sp) },
                            colors = navItemColors,
                            modifier = Modifier.testTag("tab_student_progress")
                        )
                        NavigationBarItem(
                            selected = selectedStudentTab == 3,
                            onClick = { selectedStudentTab = 3 },
                            icon = { Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Appointments") },
                            label = { Text("ADVISING", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.8.sp) },
                            colors = navItemColors,
                            modifier = Modifier.testTag("tab_student_appointments")
                        )
                    } else {
                        // Faculty Admin Tabs
                        NavigationBarItem(
                            selected = selectedAdminTab == 0,
                            onClick = { selectedAdminTab = 0 },
                            icon = { Icon(imageVector = Icons.Default.Dashboard, contentDescription = "Dashboard") },
                            label = { Text("DASHBOARD", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.8.sp) },
                            colors = navItemColors,
                            modifier = Modifier.testTag("tab_admin_dashboard")
                        )
                        NavigationBarItem(
                            selected = selectedAdminTab == 1,
                            onClick = { selectedAdminTab = 1 },
                            icon = { Icon(imageVector = Icons.Default.Group, contentDescription = "Roster") },
                            label = { Text("ROSTER", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.8.sp) },
                            colors = navItemColors,
                            modifier = Modifier.testTag("tab_admin_roster")
                        )
                        NavigationBarItem(
                            selected = selectedAdminTab == 2,
                            onClick = { selectedAdminTab = 2 },
                            icon = { Icon(imageVector = Icons.Default.Work, contentDescription = "Content") },
                            label = { Text("CONTENT", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.8.sp) },
                            colors = navItemColors,
                            modifier = Modifier.testTag("tab_admin_content")
                        )
                        NavigationBarItem(
                            selected = selectedAdminTab == 3,
                            onClick = { selectedAdminTab = 3 },
                            icon = { Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Appointments") },
                            label = { Text("ADVISING", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.8.sp) },
                            colors = navItemColors,
                            modifier = Modifier.testTag("tab_admin_appointments")
                        )
                        NavigationBarItem(
                            selected = selectedAdminTab == 4,
                            onClick = { selectedAdminTab = 4 },
                            icon = { Icon(imageVector = Icons.Default.Storage, contentDescription = "Cloud") },
                            label = { Text("CLOUD", fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.8.sp) },
                            colors = navItemColors,
                            modifier = Modifier.testTag("tab_admin_cloud")
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!isFacultyAdmin) {
                // Student View
                when (selectedStudentTab) {
                    0 -> {
                        val latestResult = currentStudentResults.firstOrNull()
                        val upcomingAppointment = currentUserAppointments.firstOrNull()
                        val recommendedCareers = if (latestResult != null) {
                            viewModel.getMatchedCareersForResult(latestResult)
                        } else {
                            allCareers
                        }

                        StudentHomeScreen(
                            student = currentProfile,
                            latestResult = latestResult,
                            upcomingAppointment = upcomingAppointment,
                            activeDeadlines = allDeadlines,
                            recommendedCareers = recommendedCareers,
                            onStartTest = {
                                viewModel.startNewAssessment()
                                selectedStudentTab = 1
                            },
                            onViewResult = { result ->
                                viewModel.setViewingResult(result)
                            },
                            onDownloadPdf = { result ->
                                viewModel.exportAndSharePdf(context, result)
                            },
                            onNavigateToAppointments = {
                                selectedStudentTab = 3
                            },
                            onSendAppointmentEmail = { appointment ->
                                viewModel.sendAppointmentEmailReminder(context, appointment)
                            }
                        )
                    }

                    1 -> {
                        AssessmentScreen(
                            questions = activeQuestions,
                            answers = testAnswers,
                            currentIndex = currentQuestionIndex,
                            isSubmitting = isSubmittingTest,
                            onAnswerSelected = { qId, rating ->
                                viewModel.answerQuestion(qId, rating)
                            },
                            onNext = {
                                viewModel.goToNextQuestion(activeQuestions.size)
                            },
                            onPrevious = {
                                viewModel.goToPreviousQuestion()
                            },
                            onSubmit = {
                                viewModel.submitAssessment { result ->
                                    selectedStudentTab = 0
                                    Toast.makeText(context, "Assessment successfully submitted!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onExit = {
                                selectedStudentTab = 0
                            }
                        )
                    }

                    2 -> {
                        ProgressDashboardScreen(
                            student = currentProfile,
                            assessmentHistory = currentStudentResults,
                            onViewResult = { result ->
                                viewModel.setViewingResult(result)
                            },
                            onDownloadPdf = { result ->
                                viewModel.exportAndSharePdf(context, result)
                            }
                        )
                    }

                    3 -> {
                        StudentAppointmentsScreen(
                            student = currentProfile,
                            appointments = currentUserAppointments,
                            deadlines = allDeadlines,
                            onScheduleAppointment = { topic, time, loc, notes ->
                                viewModel.scheduleAppointment(topic, time, loc, notes)
                                Toast.makeText(context, "Session booked with Counselor!", Toast.LENGTH_SHORT).show()
                            },
                            onSendAppointmentEmail = { appointment ->
                                viewModel.sendAppointmentEmailReminder(context, appointment)
                            },
                            onSendDeadlineEmail = { deadline ->
                                val email = currentProfile?.email ?: "student@campus-advising.edu"
                                val name = currentProfile?.name ?: "Student"
                                viewModel.sendDeadlineEmailReminder(context, deadline, email, name)
                            }
                        )
                    }
                }
            } else {
                // Faculty Admin View
                when (selectedAdminTab) {
                    0 -> {
                        val studentProfiles = allProfiles.filter { it.role == UserRole.STUDENT }
                        AdminDashboardScreen(
                            adminProfile = currentProfile,
                            allStudents = studentProfiles,
                            allResults = allResults,
                            allCareers = allCareers,
                            cloudSyncStatus = cloudSyncStatus,
                            onNavigateToRoster = { selectedAdminTab = 1 },
                            onNavigateToContent = { selectedAdminTab = 2 },
                            onNavigateToCloud = { selectedAdminTab = 4 },
                            onTriggerCloudSync = {
                                viewModel.performCloudSync()
                                Toast.makeText(context, "Cloud sync triggered.", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    1 -> {
                        AdminStudentRosterScreen(
                            results = allResults,
                            searchQuery = rosterSearchQuery,
                            selectedFilterLevel = rosterFilterLevel,
                            onSearchChange = { viewModel.setRosterSearch(it) },
                            onFilterLevelChange = { viewModel.setRosterFilterLevel(it) },
                            onViewResult = { result ->
                                viewModel.setViewingResult(result)
                            },
                            onDownloadPdf = { result ->
                                viewModel.exportAndSharePdf(context, result)
                            }
                        )
                    }

                    2 -> {
                        AdminContentManagementScreen(
                            questions = allQuestions,
                            careers = allCareers,
                            onSaveQuestion = { q ->
                                viewModel.saveQuestion(q)
                                Toast.makeText(context, "Question updated.", Toast.LENGTH_SHORT).show()
                            },
                            onDeleteQuestion = { id ->
                                viewModel.deleteQuestion(id)
                                Toast.makeText(context, "Question deleted.", Toast.LENGTH_SHORT).show()
                            },
                            onSaveCareer = { c ->
                                viewModel.saveCareer(c)
                                Toast.makeText(context, "Career path updated.", Toast.LENGTH_SHORT).show()
                            },
                            onDeleteCareer = { id ->
                                viewModel.deleteCareer(id)
                                Toast.makeText(context, "Career path deleted.", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    3 -> {
                        StudentAppointmentsScreen(
                            student = currentProfile,
                            appointments = currentUserAppointments,
                            deadlines = allDeadlines,
                            onScheduleAppointment = { topic, time, loc, notes ->
                                viewModel.scheduleAppointment(topic, time, loc, notes)
                                Toast.makeText(context, "Counseling session added to department calendar.", Toast.LENGTH_SHORT).show()
                            },
                            onSendAppointmentEmail = { appointment ->
                                viewModel.sendAppointmentEmailReminder(context, appointment)
                            },
                            onSendDeadlineEmail = { deadline ->
                                val email = currentProfile?.email ?: "students@campus-advising.edu"
                                val name = "Student Body"
                                viewModel.sendDeadlineEmailReminder(context, deadline, email, name)
                            }
                        )
                    }

                    4 -> {
                        CloudBackupScreen(
                            cloudSyncStatus = cloudSyncStatus,
                            onTriggerCloudSync = {
                                viewModel.performCloudSync()
                            },
                            onExportBackup = {
                                viewModel.exportCloudBackup(context)
                            }
                        )
                    }
                }
            }
        }
    }

    // RBAC Profile & Role Switcher Dialog
    if (showProfileSwitcher) {
        ProfileRoleSwitcherDialog(
            currentProfile = currentProfile,
            profiles = allProfiles,
            onSelectProfile = { profile ->
                viewModel.switchProfile(profile)
                selectedStudentTab = 0
                selectedAdminTab = 0
            },
            onCreateStudent = { name, email, level, major, industries ->
                viewModel.createStudentProfile(name, email, level, major, industries)
                selectedStudentTab = 0
                Toast.makeText(context, "Student profile created for $name", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showProfileSwitcher = false }
        )
    }
}
