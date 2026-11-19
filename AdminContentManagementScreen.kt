package com.example.ui.screens.admin

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssessmentQuestion
import com.example.data.model.CareerPath
import com.example.data.model.PersonalityTrait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContentManagementScreen(
    questions: List<AssessmentQuestion>,
    careers: List<CareerPath>,
    onSaveQuestion: (AssessmentQuestion) -> Unit,
    onDeleteQuestion: (Long) -> Unit,
    onSaveCareer: (CareerPath) -> Unit,
    onDeleteCareer: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Questions, 1 = Career Paths

    // Question Dialog State
    var showQuestionDialog by remember { mutableStateOf(false) }
    var editingQuestion by remember { mutableStateOf<AssessmentQuestion?>(null) }
    var qText by remember { mutableStateOf("") }
    var qScenario by remember { mutableStateOf("") }
    var qTrait by remember { mutableStateOf(PersonalityTrait.INVESTIGATIVE) }
    var qTraitDropdownExpanded by remember { mutableStateOf(false) }

    // Career Dialog State
    var showCareerDialog by remember { mutableStateOf(false) }
    var editingCareer by remember { mutableStateOf<CareerPath?>(null) }
    var cTitle by remember { mutableStateOf("") }
    var cIndustry by remember { mutableStateOf("") }
    var cPrimaryTrait by remember { mutableStateOf(PersonalityTrait.INVESTIGATIVE) }
    var cSecondaryTrait by remember { mutableStateOf(PersonalityTrait.ARTISTIC) }
    var cEducation by remember { mutableStateOf("") }
    var cSalary by remember { mutableStateOf("") }
    var cOutlook by remember { mutableStateOf("") }
    var cSkills by remember { mutableStateOf("") }
    var cMajors by remember { mutableStateOf("") }
    var cDesc by remember { mutableStateOf("") }
    var cRoadmap by remember { mutableStateOf("") }
    var pTraitDropdownExpanded by remember { mutableStateOf(false) }
    var sTraitDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTab == 0) {
                        editingQuestion = null
                        qText = ""
                        qScenario = ""
                        qTrait = PersonalityTrait.INVESTIGATIVE
                        showQuestionDialog = true
                    } else {
                        editingCareer = null
                        cTitle = ""
                        cIndustry = ""
                        cPrimaryTrait = PersonalityTrait.INVESTIGATIVE
                        cSecondaryTrait = PersonalityTrait.ARTISTIC
                        cEducation = "Bachelor's Degree in Related Field"
                        cSalary = "$80,000 - $120,000 / year"
                        cOutlook = "High Growth (+15%)"
                        cSkills = ""
                        cMajors = ""
                        cDesc = ""
                        cRoadmap = ""
                        showCareerDialog = true
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_add_content")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (selectedTab == 0) "Add Question" else "Add Career")
                }
            }
        },
        modifier = modifier.testTag("admin_content_management_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Questions (${questions.size})")
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Work, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Career Paths (${careers.size})")
                        }
                    }
                )
            }

            if (selectedTab == 0) {
                // Questions List
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "Assessment Question Bank (RIASEC Inventory)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage statements evaluated on a 1-5 Likert scale for personality trait classification.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items(questions) { question ->
                        val traitColor = Color(question.trait.colorHex)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = traitColor.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "${question.trait.title} (${question.trait.code})",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            color = traitColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Row {
                                        IconButton(onClick = {
                                            editingQuestion = question
                                            qText = question.questionText
                                            qScenario = question.studentScenario
                                            qTrait = question.trait
                                            showQuestionDialog = true
                                        }) {
                                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                                        }
                                        IconButton(onClick = { onDeleteQuestion(question.id) }) {
                                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = question.questionText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Scenario: ${question.studentScenario}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(64.dp)) }
                }
            } else {
                // Careers List
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "Department Curated Career Database",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Update job paths, salary brackets, required skills, and academic roadmaps.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items(careers) { career ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = career.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${career.industry} • ${career.primaryTrait.title} & ${career.secondaryTrait.title}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Row {
                                        IconButton(onClick = {
                                            editingCareer = career
                                            cTitle = career.title
                                            cIndustry = career.industry
                                            cPrimaryTrait = career.primaryTrait
                                            cSecondaryTrait = career.secondaryTrait
                                            cEducation = career.educationRequirement
                                            cSalary = career.salaryRange
                                            cOutlook = career.growthOutlook
                                            cSkills = career.keySkills
                                            cMajors = career.recommendedMajors
                                            cDesc = career.description
                                            cRoadmap = career.academicRoadmap
                                            showCareerDialog = true
                                        }) {
                                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                                        }
                                        IconButton(onClick = { onDeleteCareer(career.id) }) {
                                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = career.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text(
                                        text = "💰 ${career.salaryRange}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "📈 ${career.growthOutlook}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF10B981),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(64.dp)) }
                }
            }
        }
    }

    // Question Dialog
    if (showQuestionDialog) {
        AlertDialog(
            onDismissRequest = { showQuestionDialog = false },
            title = {
                Text(if (editingQuestion == null) "Add Assessment Question" else "Edit Question")
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = qText,
                        onValueChange = { qText = it },
                        label = { Text("Question Statement") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = qScenario,
                        onValueChange = { qScenario = it },
                        label = { Text("Student Scenario / Example") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = qTraitDropdownExpanded,
                        onExpandedChange = { qTraitDropdownExpanded = !qTraitDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = "${qTrait.title} (${qTrait.code})",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Personality Trait Dimension") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = qTraitDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = qTraitDropdownExpanded,
                            onDismissRequest = { qTraitDropdownExpanded = false }
                        ) {
                            PersonalityTrait.entries.forEach { trait ->
                                DropdownMenuItem(
                                    text = { Text("${trait.title} (${trait.code})") },
                                    onClick = {
                                        qTrait = trait
                                        qTraitDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val question = editingQuestion?.copy(
                            questionText = qText.trim(),
                            studentScenario = qScenario.trim(),
                            trait = qTrait
                        ) ?: AssessmentQuestion(
                            questionText = qText.trim(),
                            studentScenario = qScenario.trim(),
                            trait = qTrait
                        )
                        onSaveQuestion(question)
                        showQuestionDialog = false
                    },
                    enabled = qText.isNotBlank()
                ) {
                    Text("Save Question")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuestionDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Career Dialog
    if (showCareerDialog) {
        AlertDialog(
            onDismissRequest = { showCareerDialog = false },
            title = {
                Text(if (editingCareer == null) "Add Career Path" else "Edit Career Path")
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = cTitle,
                        onValueChange = { cTitle = it },
                        label = { Text("Job Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cIndustry,
                        onValueChange = { cIndustry = it },
                        label = { Text("Industry Category") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Primary Trait
                    ExposedDropdownMenuBox(
                        expanded = pTraitDropdownExpanded,
                        onExpandedChange = { pTraitDropdownExpanded = !pTraitDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = "Primary: ${cPrimaryTrait.title}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Primary RIASEC Trait") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = pTraitDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = pTraitDropdownExpanded,
                            onDismissRequest = { pTraitDropdownExpanded = false }
                        ) {
                            PersonalityTrait.entries.forEach { trait ->
                                DropdownMenuItem(
                                    text = { Text(trait.title) },
                                    onClick = {
                                        cPrimaryTrait = trait
                                        pTraitDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secondary Trait
                    ExposedDropdownMenuBox(
                        expanded = sTraitDropdownExpanded,
                        onExpandedChange = { sTraitDropdownExpanded = !sTraitDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = "Secondary: ${cSecondaryTrait.title}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Secondary RIASEC Trait") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sTraitDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = sTraitDropdownExpanded,
                            onDismissRequest = { sTraitDropdownExpanded = false }
                        ) {
                            PersonalityTrait.entries.forEach { trait ->
                                DropdownMenuItem(
                                    text = { Text(trait.title) },
                                    onClick = {
                                        cSecondaryTrait = trait
                                        sTraitDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cSalary,
                        onValueChange = { cSalary = it },
                        label = { Text("Salary Range") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cEducation,
                        onValueChange = { cEducation = it },
                        label = { Text("Education Requirement") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cSkills,
                        onValueChange = { cSkills = it },
                        label = { Text("Key Skills (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cMajors,
                        onValueChange = { cMajors = it },
                        label = { Text("Recommended College Majors") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cDesc,
                        onValueChange = { cDesc = it },
                        label = { Text("Career Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cRoadmap,
                        onValueChange = { cRoadmap = it },
                        label = { Text("Academic & Career Roadmap") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val career = editingCareer?.copy(
                            title = cTitle.trim(),
                            industry = cIndustry.trim(),
                            primaryTrait = cPrimaryTrait,
                            secondaryTrait = cSecondaryTrait,
                            salaryRange = cSalary.trim(),
                            growthOutlook = cOutlook.trim(),
                            educationRequirement = cEducation.trim(),
                            keySkills = cSkills.trim(),
                            recommendedMajors = cMajors.trim(),
                            description = cDesc.trim(),
                            academicRoadmap = cRoadmap.trim()
                        ) ?: CareerPath(
                            title = cTitle.trim(),
                            industry = cIndustry.trim(),
                            primaryTrait = cPrimaryTrait,
                            secondaryTrait = cSecondaryTrait,
                            salaryRange = cSalary.trim(),
                            growthOutlook = cOutlook.trim(),
                            educationRequirement = cEducation.trim(),
                            keySkills = cSkills.trim(),
                            recommendedMajors = cMajors.trim(),
                            description = cDesc.trim(),
                            academicRoadmap = cRoadmap.trim()
                        )
                        onSaveCareer(career)
                        showCareerDialog = false
                    },
                    enabled = cTitle.isNotBlank() && cIndustry.isNotBlank()
                ) {
                    Text("Save Career")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCareerDialog = false }) { Text("Cancel") }
            }
        )
    }
}
