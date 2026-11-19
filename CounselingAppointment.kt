package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class CounselingAppointment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val studentEmail: String,
    val counselorName: String,
    val appointmentType: String,
    val timestamp: Long,
    val durationMinutes: Int = 45,
    val location: String = "Academic Career Advising Hall, Suite 210",
    val status: String = "SCHEDULED", // SCHEDULED, COMPLETED, CANCELLED
    val notes: String = "",
    val reminderEmailSent: Boolean = false
)

@Entity(tableName = "assessment_deadlines")
data class AssessmentDeadline(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val targetAudience: String,
    val dueDate: Long,
    val description: String,
    val isUrgent: Boolean = false,
    val reminderDispatched: Boolean = false
)
