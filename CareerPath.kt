package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "careers")
data class CareerPath(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val industry: String,
    val primaryTrait: PersonalityTrait,
    val secondaryTrait: PersonalityTrait,
    val educationRequirement: String, // e.g. "Bachelor's Degree in Computer Science or related"
    val salaryRange: String, // e.g. "$92,000 - $145,000 / year"
    val growthOutlook: String, // e.g. "Much faster than average (+25%)"
    val keySkills: String, // "Data Structures, Cloud Systems, Problem Solving, Kotlin/Python"
    val recommendedMajors: String, // "Computer Science, Software Engineering, Cognitive Science"
    val description: String,
    val academicRoadmap: String, // "Build GitHub portfolio in college, take AP CS in high school, pursue summer internships"
    val specializations: String = "", // e.g. "Fine Artist, Illustrator, Portrait Artist, Sculptor"
    val psyAnalysisRating: String = "High: 75", // e.g. "Very High: 100"
    val skillsAbilitiesRating: String = "High: 65", // e.g. "High: 60"
    val recommendationComment: String = "Top Choice", // "Top Choice" or "Good Choice"
    val isCustomAdded: Boolean = false // Track if added by counselor faculty in admin panel
)
