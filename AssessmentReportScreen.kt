package com.example.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import com.example.data.model.AssessmentResult
import com.example.data.model.CareerPath
import com.example.data.model.PersonalityTrait
import com.example.data.model.UserRole
import com.example.ui.components.CareerCard
import com.example.ui.components.TraitProgressBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentReportScreen(
    result: AssessmentResult,
    matchedCareers: List<CareerPath>,
    currentUserRole: UserRole,
    onDownloadPdf: () -> Unit,
    onBookDebrief: () -> Unit,
    onSaveCounselorNotes: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditingNotes by remember { mutableStateOf(false) }
    var notesText by remember { mutableStateOf(result.counselorNotes) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Assessment Report",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Official Academic Career Guidance Record",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = onDownloadPdf,
                        modifier = Modifier.testTag("btn_top_bar_download_pdf")
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = "Download Report PDF")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier.testTag("assessment_report_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Hero Banner
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF1E3A8A),
                                        Color(0xFF0284C7)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "HOLLAND CODE ARCHETYPE",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFF59E0B)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MilitaryTech,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = result.hollandCode,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${result.primaryTrait.title} & ${result.secondaryTrait.title}",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Student: ${result.studentName} • ${result.educationLevel.displayName}",
                                color = Color(0xFFE0F2FE),
                                style = MaterialTheme.typography.bodySmall
                            )

                            val dateFormat = SimpleDateFormat("MMMM dd, yyyy • hh:mm a", Locale.getDefault())
                            Text(
                                text = "Assessed: ${dateFormat.format(Date(result.timestamp))} • Career Readiness: ${result.careerReadinessScore}%",
                                color = Color(0xFFBAE6FD),
                                style = MaterialTheme.typography.labelSmall
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = onDownloadPdf,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("btn_report_download_pdf"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color(0xFF1E3A8A)
                                    )
                                ) {
                                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Download PDF Report", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }

                                OutlinedButton(
                                    onClick = onBookDebrief,
                                    modifier = Modifier.height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.White
                                    )
                                ) {
                                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Book Session", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Section 1: Planning Stage ("Where Do You Stand Right Now?")
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("card_planning_stage"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (result.planningStage) {
                            "Confused" -> Color(0xFFFEF2F2)
                            "Exploring" -> Color(0xFFFEF9C3)
                            else -> Color(0xFFF0FDF4)
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = when (result.planningStage) {
                                        "Confused" -> Color(0xFFEF4444)
                                        "Exploring" -> Color(0xFFD97706)
                                        else -> Color(0xFF16A34A)
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "1. WHERE DO YOU STAND RIGHT NOW?",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (result.planningStage) {
                                    "Confused" -> Color(0xFFEF4444)
                                    "Exploring" -> Color(0xFFD97706)
                                    else -> Color(0xFF16A34A)
                                }
                            ) {
                                Text(
                                    text = result.planningStage.uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Risk of Being in this Stage:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )
                        Text(
                            text = result.planningStageRisk.ifEmpty { "Wrong selection of a career path, career dissatisfaction, and mismatch with your interests." },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF475569),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Recommended 5-Step Action Roadmap:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                        Text(
                            text = result.planningStageActionPlan.ifEmpty { "Explore strengths & weaknesses > Explore career options > Gather information > Match suitable options > Early execution." },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1E3A8A),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Section 2: Career Personality (MBTI)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("card_mbti_personality"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = Color(0xFF7C3AED),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "2. Career Personality Profile (16 Types)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Archetype: ${result.personalityType} • ${result.personalityTypeFullName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF7C3AED),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        MbtiSpectrumRow(
                            dimension = "Energy Focus",
                            leftLabel = "Extrovert (E)",
                            leftPct = result.extrovertScore,
                            rightLabel = "Introvert (I)",
                            rightPct = result.introvertScore
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MbtiSpectrumRow(
                            dimension = "Information Input",
                            leftLabel = "Sensing (S)",
                            leftPct = result.sensingScore,
                            rightLabel = "iNtuitive (N)",
                            rightPct = result.intuitiveScore
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MbtiSpectrumRow(
                            dimension = "Decision Making",
                            leftLabel = "Thinking (T)",
                            leftPct = result.thinkingScore,
                            rightLabel = "Feeling (F)",
                            rightPct = result.feelingScore
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MbtiSpectrumRow(
                            dimension = "Work Planning",
                            leftLabel = "Judging (J)",
                            leftPct = result.judgingScore,
                            rightLabel = "Perceiving (P)",
                            rightPct = result.perceivingScore
                        )
                    }
                }
            }

            // Personality Trait Breakdown Section (Section 3: RIASEC)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("card_riasec_breakdown"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "3. RIASEC Personality Trait Breakdown (Holland Codes)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Holland Code: ${result.hollandCode} • Primary: ${result.primaryTrait.title} • Secondary: ${result.secondaryTrait.title}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TraitProgressBar(
                            trait = PersonalityTrait.ARTISTIC,
                            score = result.artisticScore,
                            showSummary = true
                        )
                        TraitProgressBar(
                            trait = PersonalityTrait.SOCIAL,
                            score = result.socialScore,
                            showSummary = true
                        )
                        TraitProgressBar(
                            trait = PersonalityTrait.INVESTIGATIVE,
                            score = result.investigativeScore,
                            showSummary = true
                        )
                        TraitProgressBar(
                            trait = PersonalityTrait.REALISTIC,
                            score = result.realisticScore,
                            showSummary = true
                        )
                        TraitProgressBar(
                            trait = PersonalityTrait.CONVENTIONAL,
                            score = result.conventionalScore,
                            showSummary = true
                        )
                        TraitProgressBar(
                            trait = PersonalityTrait.ENTERPRISING,
                            score = result.enterprisingScore,
                            showSummary = true
                        )
                    }
                }
            }

            // Section 4: Core Career Motivators
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("card_career_motivators"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "4. Core Career Motivators",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Key psychological and operational drivers of career fulfillment",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        MetricBarRow("Creativity & Innovation", result.motivatorCreativity, Color(0xFF8B5CF6))
                        MetricBarRow("Independence & Autonomy", result.motivatorIndependence, Color(0xFF4F46E5))
                        MetricBarRow("Adventure & Novelty", result.motivatorAdventure, Color(0xFFEA580C))
                        MetricBarRow("Social Service & Impact", result.motivatorSocialService, Color(0xFF10B981))
                        MetricBarRow("Continuous Learning", result.motivatorContinuousLearning, Color(0xFF2563EB))
                        MetricBarRow("Structured Environment", result.motivatorStructuredEnvironment, Color(0xFF64748B))
                        MetricBarRow("High-Paced Setting", result.motivatorHighPaced, Color(0xFFF59E0B))
                    }
                }
            }

            // Section 5: Learning Styles (VARK)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("card_learning_styles"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AutoStories, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "5. Learning Style Analysis (VARK Model)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Optimal intake methods for academic retention and study performance",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        MetricBarRow("Visual (Diagrams & Flowcharts)", result.learningStyleVisual, Color(0xFF3B82F6))
                        MetricBarRow("Auditory (Lectures & Discussions)", result.learningStyleAuditory, Color(0xFF10B981))
                        MetricBarRow("Read / Write (Textbooks & Notes)", result.learningStyleReadWrite, Color(0xFF8B5CF6))
                        MetricBarRow("Kinesthetic (Hands-on & Tactile)", result.learningStyleKinesthetic, Color(0xFFF97316))
                    }
                }
            }

            // Section 6: Core Skills & Abilities
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("card_skills_abilities"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = Color(0xFF0D9488), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "6. Skills & Abilities Assessment",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE6FFFA)) {
                                Text(
                                    text = "Avg: ${result.overallSkillsAverage}%",
                                    color = Color(0xFF0D9488),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Text(
                            text = "Normalized competency evaluation across 8 cognitive & practical skillsets",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        MetricBarRow("Verbal Fluency (Language)", result.skillVerbal, Color(0xFF10B981))
                        MetricBarRow("Spatial & 3D Visualization", result.skillSpatial, Color(0xFF2563EB))
                        MetricBarRow("Mechanical Intuition", result.skillMechanical, Color(0xFFEA580C))
                        MetricBarRow("Leadership & Decisiveness", result.skillLeadership, Color(0xFF8B5CF6))
                        MetricBarRow("Logical & Abstract Reasoning", result.skillLogical, Color(0xFF06B6D4))
                        MetricBarRow("Social Cooperation & Empathy", result.skillSocialCooperation, Color(0xFFEC4899))
                        MetricBarRow("Administrative Coordination", result.skillAdministrative, Color(0xFF64748B))
                        MetricBarRow("Numerical & Quantitative", result.skillNumerical, Color(0xFFEF4444))
                    }
                }
            }

            // Section 7: Academic Stream Selection Matrix
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("card_stream_matrix"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.School, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "7. Academic Stream Selection Matrix",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Suitability matching for secondary/higher-secondary stream selection (Humanities vs Science vs Commerce)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        MetricBarRow("Humanities & Liberal Arts (Top Choice)", result.humanitiesMatch, Color(0xFF10B981))
                        MetricBarRow("Science (Maths / Computer Tech)", result.scienceMathsMatch, Color(0xFF3B82F6))
                        MetricBarRow("Science (Biology / Medical)", result.scienceBioMatch, Color(0xFFF59E0B))
                        MetricBarRow("Commerce & Management", result.commerceMatch, Color(0xFF94A3B8))
                    }
                }
            }

            // Counselor Remarks & Academic Endorsement
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("counselor_notes_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Academic Counselor Endorsement",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (currentUserRole == UserRole.ADMIN) {
                                IconButton(onClick = { isEditingNotes = !isEditingNotes }) {
                                    Icon(
                                        imageVector = if (isEditingNotes) Icons.Default.Check else Icons.Default.Edit,
                                        contentDescription = "Edit Notes",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isEditingNotes && currentUserRole == UserRole.ADMIN) {
                            OutlinedTextField(
                                value = notesText,
                                onValueChange = { notesText = it },
                                label = { Text("Counselor Notes & Guidance Recommendations") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    onSaveCounselorNotes(notesText)
                                    isEditingNotes = false
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Save Counselor Review")
                            }
                        } else {
                            Text(
                                text = if (result.counselorNotes.isNotBlank()) {
                                    result.counselorNotes
                                } else {
                                    "Pending formal counselor debrief. Student has been matched with high school / college career pathways matching archetype ${result.hollandCode}."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            val counselorName = if (result.counselorReviewedBy.isNotBlank()) result.counselorReviewedBy else "Academic Counseling Department Staff"
                            Text(
                                text = "✍️ Reviewed & Signed by: $counselorName",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Section Header: Top Matched Careers
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Recommended Career & Job Paths",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Ranked by Holland trait alignment and academic suitability",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Careers List
            items(matchedCareers) { career ->
                val pScore = when (career.primaryTrait) {
                    PersonalityTrait.REALISTIC -> result.realisticScore
                    PersonalityTrait.INVESTIGATIVE -> result.investigativeScore
                    PersonalityTrait.ARTISTIC -> result.artisticScore
                    PersonalityTrait.SOCIAL -> result.socialScore
                    PersonalityTrait.ENTERPRISING -> result.enterprisingScore
                    PersonalityTrait.CONVENTIONAL -> result.conventionalScore
                }
                val sScore = when (career.secondaryTrait) {
                    PersonalityTrait.REALISTIC -> result.realisticScore
                    PersonalityTrait.INVESTIGATIVE -> result.investigativeScore
                    PersonalityTrait.ARTISTIC -> result.artisticScore
                    PersonalityTrait.SOCIAL -> result.socialScore
                    PersonalityTrait.ENTERPRISING -> result.enterprisingScore
                    PersonalityTrait.CONVENTIONAL -> result.conventionalScore
                }
                val matchPct = (pScore * 0.65f + sScore * 0.35f).toInt()

                CareerCard(career = career, matchPercentage = matchPct)
            }
        }
    }
}

@Composable
fun MbtiSpectrumRow(
    dimension: String,
    leftLabel: String,
    leftPct: Int,
    rightLabel: String,
    rightPct: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = dimension,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight((leftPct.coerceAtLeast(5)).toFloat())
                    .background(if (leftPct >= rightPct) Color(0xFF4F46E5) else Color(0xFFA5B4FC))
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight((rightPct.coerceAtLeast(5)).toFloat())
                    .background(if (rightPct >= leftPct) Color(0xFF7C3AED) else Color(0xFFC4B5FD))
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$leftLabel $leftPct%",
                style = MaterialTheme.typography.labelSmall,
                color = if (leftPct >= rightPct) Color(0xFF4F46E5) else Color(0xFF64748B),
                fontWeight = if (leftPct >= rightPct) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.sp
            )
            Text(
                text = "$rightLabel $rightPct%",
                style = MaterialTheme.typography.labelSmall,
                color = if (rightPct >= leftPct) Color(0xFF7C3AED) else Color(0xFF64748B),
                fontWeight = if (rightPct >= leftPct) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun MetricBarRow(
    label: String,
    score: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .width(130.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(score.coerceIn(0, 100) / 100f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$score%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.width(36.dp)
        )
    }
}
