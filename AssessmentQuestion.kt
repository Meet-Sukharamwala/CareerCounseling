package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessment_questions")
data class AssessmentQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionText: String,
    val studentScenario: String,
    val section: String = "CAREER_INTEREST", // "PLANNING_STAGE", "CAREER_PERSONALITY", "CAREER_INTEREST", "CAREER_MOTIVATOR", "LEARNING_STYLE", "SKILLS_ABILITIES"
    val dimensionKey: String = "A", // e.g. "STAGE", "EI", "SN", "TF", "JP", "R", "I", "A", "S", "E", "C", "CREATIVITY", "INDEPENDENCE", etc.
    val dimensionTitle: String = "Artistic",
    val trait: PersonalityTrait = PersonalityTrait.ARTISTIC,
    val traitWeight: Float = 1.0f,
    val sortOrder: Int = 0,
    val isActive: Boolean = true
)
