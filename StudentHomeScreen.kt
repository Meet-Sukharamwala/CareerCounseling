package com.example.ui.screens.student

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssessmentDeadline
import com.example.data.model.AssessmentResult
import com.example.data.model.CareerPath
import com.example.data.model.CounselingAppointment
import com.example.data.model.UserProfile
import com.example.ui.components.CareerCard
import com.example.ui.components.TraitProgressBar
import com.example.ui.theme.BoldBackground
import com.example.ui.theme.BoldCharcoal
import com.example.ui.theme.BoldDarkInk
import com.example.ui.theme.BoldLavenderRing
import com.example.ui.theme.BoldLilacAccent
import com.example.ui.theme.BoldMediumNeutral
import com.example.ui.theme.BoldOutline
import com.example.ui.theme.BoldPinkAccent
import com.example.ui.theme.BoldPurple
import com.example.ui.theme.BoldPurpleContainer
import com.example.ui.theme.BoldPurpleDark
import com.example.ui.theme.BoldPurpleLight
import com.example.ui.theme.BoldSurfaceVariant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StudentHomeScreen(
    student: UserProfile?,
    latestResult: AssessmentResult?,
    upcomingAppointment: CounselingAppointment?,
    activeDeadlines: List<AssessmentDeadline>,
    recommendedCareers: List<CareerPath>,
    onStartTest: () -> Unit,
    onViewResult: (AssessmentResult) -> Unit,
    onDownloadPdf: (AssessmentResult) -> Unit,
    onNavigateToAppointments: () -> Unit,
    onSendAppointmentEmail: (CounselingAppointment) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentStudent = student ?: return

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("student_home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Greeting Card (Bold Typography 32px rounded container)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_greeting_card"),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BoldPurpleLight
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = BoldPurpleDark
                        ) {
                            Text(
                                text = if (latestResult != null) "ASSESSMENT LIVE" else "ASSESSMENT READY",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = currentStudent.educationLevel.displayName.uppercase(),
                                color = BoldPurpleDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = if (latestResult != null) {
                            "${latestResult.careerReadinessScore}%\nREADINESS"
                        } else {
                            "DISCOVER\nYOUR PATH"
                        },
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = BoldPurpleDark,
                        lineHeight = 38.sp,
                        letterSpacing = (-1).sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (latestResult != null) {
                                "Personality Matrix: ${latestResult.hollandCode} • ${latestResult.primaryTrait.title}"
                            } else {
                                "Personality Matrix: RIASEC Diagnostic (18 Qs)"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = BoldPurpleDark.copy(alpha = 0.85f),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { onStartTest() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Start Assessment",
                                tint = BoldPurpleDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onStartTest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_hero_take_assessment"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BoldPurpleDark,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (latestResult == null) "TAKE CAREER APTITUDE TEST" else "RETAKE CAREER TEST",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // 2-Column Grid (Top Industry & Next Step)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Industry Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BoldOutline)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "TOP INDUSTRY",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BoldMediumNeutral,
                            letterSpacing = 1.2.sp
                        )
                        val topIndustryName = recommendedCareers.firstOrNull()?.industry ?: "CREATIVE TECH"
                        Text(
                            text = topIndustryName.uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            lineHeight = 20.sp,
                            maxLines = 2
                        )
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(BoldLavenderRing)
                                    .border(2.dp, Color.White, CircleShape)
                            )
                            Spacer(modifier = Modifier.width((-6).dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(BoldLilacAccent)
                                    .border(2.dp, Color.White, CircleShape)
                            )
                        }
                    }
                }

                // Next Step Charcoal Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                        .clickable { onNavigateToAppointments() },
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = BoldCharcoal)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "NEXT STEP",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.7f),
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = if (upcomingAppointment != null) "FACULTY\nSYNC" else "BOOK\nADVISING",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = Color.White,
                            lineHeight = 20.sp
                        )
                        val timeStr = if (upcomingAppointment != null) {
                            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                            timeFormat.format(Date(upcomingAppointment.timestamp))
                        } else "Available Today"
                        Text(
                            text = timeStr,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Upcoming Counseling Session / Deadline Notification Banner
        if (upcomingAppointment != null || activeDeadlines.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("notification_banner_card"),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BoldSurfaceVariant
                    ),
                    border = BorderStroke(1.dp, BoldOutline)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = BoldPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (upcomingAppointment != null) "UPCOMING ADVISING SESSION" else "IMPENDING DEADLINE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = BoldDarkInk,
                                    letterSpacing = 1.sp
                                )
                            }

                            Text(
                                text = "VIEW ALL",
                                style = MaterialTheme.typography.labelSmall,
                                color = BoldPurple,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.clickable { onNavigateToAppointments() }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (upcomingAppointment != null) {
                            val dateFormat = SimpleDateFormat("EEE, MMM d 'at' h:mm a", Locale.getDefault())
                            val dateStr = dateFormat.format(Date(upcomingAppointment.timestamp))

                            Text(
                                text = "${upcomingAppointment.appointmentType} with ${upcomingAppointment.counselorName}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = BoldDarkInk
                            )
                            Text(
                                text = "📅 $dateStr • ${upcomingAppointment.location}",
                                style = MaterialTheme.typography.bodySmall,
                                color = BoldMediumNeutral
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { onSendAppointmentEmail(upcomingAppointment) },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, BoldOutline),
                                    modifier = Modifier
                                        .height(38.dp)
                                        .testTag("btn_email_appointment_reminder")
                                ) {
                                    Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(14.dp), tint = BoldPurple)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Send Email Reminder", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BoldDarkInk)
                                }
                            }
                        } else if (activeDeadlines.isNotEmpty()) {
                            val deadline = activeDeadlines.first()
                            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            val dateStr = dateFormat.format(Date(deadline.dueDate))

                            Text(
                                text = "${deadline.title} (Due $dateStr)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = BoldDarkInk
                            )
                            Text(
                                text = deadline.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = BoldMediumNeutral,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }

        // Latest Assessment Summary Card (Historical Progress style from Design HTML)
        item {
            if (latestResult != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("latest_assessment_result_card"),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = BoldSurfaceVariant),
                    border = BorderStroke(1.dp, BoldOutline)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "HISTORICAL PROGRESS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = BoldDarkInk,
                                    letterSpacing = 1.sp
                                )
                                val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                                Text(
                                    text = "Completed on ${dateFormat.format(Date(latestResult.timestamp))}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BoldMediumNeutral
                                )
                            }

                            // Holland Code Badge
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = BoldPurpleDark
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "CODE: ${latestResult.hollandCode}",
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Test item preview row
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(BoldPinkAccent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("📊", fontSize = 18.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Aptitude Assessment",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Primary Trait: ${latestResult.primaryTrait.title}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = BoldMediumNeutral
                                        )
                                    }
                                }

                                Text(
                                    text = "${latestResult.careerReadinessScore}/100",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Primary Traits breakdown preview
                        TraitProgressBar(
                            trait = latestResult.primaryTrait,
                            score = when (latestResult.primaryTrait) {
                                com.example.data.model.PersonalityTrait.REALISTIC -> latestResult.realisticScore
                                com.example.data.model.PersonalityTrait.INVESTIGATIVE -> latestResult.investigativeScore
                                com.example.data.model.PersonalityTrait.ARTISTIC -> latestResult.artisticScore
                                com.example.data.model.PersonalityTrait.SOCIAL -> latestResult.socialScore
                                com.example.data.model.PersonalityTrait.ENTERPRISING -> latestResult.enterprisingScore
                                com.example.data.model.PersonalityTrait.CONVENTIONAL -> latestResult.conventionalScore
                            }
                        )

                        TraitProgressBar(
                            trait = latestResult.secondaryTrait,
                            score = when (latestResult.secondaryTrait) {
                                com.example.data.model.PersonalityTrait.REALISTIC -> latestResult.realisticScore
                                com.example.data.model.PersonalityTrait.INVESTIGATIVE -> latestResult.investigativeScore
                                com.example.data.model.PersonalityTrait.ARTISTIC -> latestResult.artisticScore
                                com.example.data.model.PersonalityTrait.SOCIAL -> latestResult.socialScore
                                com.example.data.model.PersonalityTrait.ENTERPRISING -> latestResult.enterprisingScore
                                com.example.data.model.PersonalityTrait.CONVENTIONAL -> latestResult.conventionalScore
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons: View Full Report & Download PDF
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onViewResult(latestResult) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("btn_view_full_report"),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BoldPurple,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("VIEW FULL REPORT", fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 0.5.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }

                            OutlinedButton(
                                onClick = { onDownloadPdf(latestResult) },
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("btn_download_pdf_report"),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, BoldOutline)
                            ) {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp), tint = BoldDarkInk)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("PDF", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BoldDarkInk)
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = BoldSurfaceVariant),
                    border = BorderStroke(1.dp, BoldOutline)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(BoldPurpleLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assignment,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = BoldPurpleDark
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "NO ASSESSMENT YET",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.25).sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Take our 18-question personality trait assessment to discover career clusters matching your strengths.",
                            style = MaterialTheme.typography.bodySmall,
                            color = BoldMediumNeutral,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onStartTest,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BoldPurpleDark,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("btn_empty_start_test")
                        ) {
                            Text("BEGIN ASSESSMENT (5 MINS)", fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
                        }
                    }
                }
            }
        }

        // Section Title: Recommended Job Paths & Industry Matches
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECOMMENDED JOB PATHS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.25).sp,
                    color = BoldDarkInk
                )
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = BoldPurpleContainer
                ) {
                    Text(
                        text = "${recommendedCareers.size} MATCHES",
                        style = MaterialTheme.typography.labelSmall,
                        color = BoldPurpleDark,
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Recommended Careers List
        items(recommendedCareers.take(4)) { career ->
            CareerCard(
                career = career,
                matchPercentage = if (latestResult != null) {
                    // Match calculation
                    val pScore = when (career.primaryTrait) {
                        com.example.data.model.PersonalityTrait.REALISTIC -> latestResult.realisticScore
                        com.example.data.model.PersonalityTrait.INVESTIGATIVE -> latestResult.investigativeScore
                        com.example.data.model.PersonalityTrait.ARTISTIC -> latestResult.artisticScore
                        com.example.data.model.PersonalityTrait.SOCIAL -> latestResult.socialScore
                        com.example.data.model.PersonalityTrait.ENTERPRISING -> latestResult.enterprisingScore
                        com.example.data.model.PersonalityTrait.CONVENTIONAL -> latestResult.conventionalScore
                    }
                    val sScore = when (career.secondaryTrait) {
                        com.example.data.model.PersonalityTrait.REALISTIC -> latestResult.realisticScore
                        com.example.data.model.PersonalityTrait.INVESTIGATIVE -> latestResult.investigativeScore
                        com.example.data.model.PersonalityTrait.ARTISTIC -> latestResult.artisticScore
                        com.example.data.model.PersonalityTrait.SOCIAL -> latestResult.socialScore
                        com.example.data.model.PersonalityTrait.ENTERPRISING -> latestResult.enterprisingScore
                        com.example.data.model.PersonalityTrait.CONVENTIONAL -> latestResult.conventionalScore
                    }
                    (pScore * 0.65f + sScore * 0.35f).toInt()
                } else null
            )
        }
    }
}
