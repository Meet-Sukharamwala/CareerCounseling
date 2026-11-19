package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val role: UserRole,
    val educationLevel: EducationLevel = EducationLevel.COLLEGE_UNDERGRAD,
    val academicMajor: String = "Undeclared / General Studies",
    val targetIndustries: String = "Technology, Healthcare, Design",
    val assignedCounselor: String = "Dr. Sarah Jenkins",
    val departmentalTitle: String = "", // Used for faculty, e.g. "Lead Career Counselor, Dean's Office"
    val avatarColorHex: Long = 0xFF2563EB,
    val dateCreated: Long = System.currentTimeMillis()
)
