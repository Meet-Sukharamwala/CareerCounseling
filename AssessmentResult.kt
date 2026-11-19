package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessment_results")
data class AssessmentResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val studentEmail: String,
    val educationLevel: EducationLevel,
    val timestamp: Long = System.currentTimeMillis(),
    val realisticScore: Int,
    val investigativeScore: Int,
    val artisticScore: Int,
    val socialScore: Int,
    val enterprisingScore: Int,
    val conventionalScore: Int,
    val primaryTrait: PersonalityTrait,
    val secondaryTrait: PersonalityTrait,
    val hollandCode: String,
    
    // Profiling: Current Stage of Planning
    val planningStage: String = "Confused", // "Unaware", "Confused", "Exploring", "Clarity", "Future-Ready"
    val planningStageRisk: String = "Wrong selection of a career path, career dissatisfaction, and mismatch with your interests.",
    val planningStageActionPlan: String = "Explore your strengths and weaknesses > Explore career options > Gather information > Match the most suitable option > Early execution.",

    // MBTI Personality Type & Dimensions (0-100 for each pole)
    val personalityType: String = "INFP",
    val personalityTypeFullName: String = "Introvert : iNtuitive : Feeling : Perceiving (Spontaneous)",
    val introvertScore: Int = 71,
    val extrovertScore: Int = 29,
    val intuitiveScore: Int = 71,
    val sensingScore: Int = 29,
    val feelingScore: Int = 57,
    val thinkingScore: Int = 43,
    val perceivingScore: Int = 71,
    val judgingScore: Int = 29,

    // Career Motivators (0-100)
    val motivatorCreativity: Int = 100,
    val motivatorIndependence: Int = 100,
    val motivatorAdventure: Int = 60,
    val motivatorSocialService: Int = 60,
    val motivatorContinuousLearning: Int = 60,
    val motivatorStructuredEnvironment: Int = 60,
    val motivatorHighPaced: Int = 40,

    // Learning Styles (VARK) (0-100)
    val learningStyleVisual: Int = 25,
    val learningStyleAuditory: Int = 25,
    val learningStyleReadWrite: Int = 25,
    val learningStyleKinesthetic: Int = 25,

    // Skills & Abilities (8 core aptitude competencies)
    val skillNumerical: Int = 20,
    val skillLogical: Int = 40,
    val skillVerbal: Int = 100,
    val skillAdministrative: Int = 30,
    val skillSpatial: Int = 80,
    val skillLeadership: Int = 48,
    val skillSocialCooperation: Int = 40,
    val skillMechanical: Int = 50,
    val overallSkillsAverage: Int = 51,

    // Selected 4 Career Clusters
    val selectedClustersSummary: String = "Creative Arts & Design, Human Services, Architecture and Construction, Media and Communication",

    // Subject Stream Recommendations (% match)
    val humanitiesMatch: Int = 93,
    val scienceMathsMatch: Int = 61,
    val scienceBioMatch: Int = 39,
    val commerceMatch: Int = 36,

    val topCareersSummary: String, // JSON or formatted summary of top matched titles & industries
    val careerReadinessScore: Int = 85, // Overall career readiness index (0-100)
    val counselorNotes: String = "",
    val counselorReviewed: Boolean = false,
    val counselorReviewedBy: String = "",
    val counselorReviewedTimestamp: Long = 0L,
    val pdfExportPath: String? = null
)
