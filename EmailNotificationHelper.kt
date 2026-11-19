package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.model.AssessmentDeadline
import com.example.data.model.CounselingAppointment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EmailNotificationHelper {

    fun sendAppointmentEmail(
        context: Context,
        appointment: CounselingAppointment,
        isReminder: Boolean = false
    ) {
        val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(appointment.timestamp))

        val subject = if (isReminder) {
            "Appointment Reminder: Career Counseling Session on $formattedDate"
        } else {
            "Confirmed: Career Counseling Session with ${appointment.counselorName}"
        }

        val body = """
            Dear ${appointment.studentName},

            This is an automated notification from the Academic Career Counseling Department.

            Your career counseling appointment has been scheduled as follows:
            • Session Topic: ${appointment.appointmentType}
            • Counselor: ${appointment.counselorName}
            • Date & Time: $formattedDate
            • Duration: ${appointment.durationMinutes} minutes
            • Location / Meeting Link: ${appointment.location}
            
            Notes from Counselor:
            ${if (appointment.notes.isNotBlank()) appointment.notes else "Please bring your latest Career Assessment Report and any questions regarding target industries or college majors."}

            If you need to reschedule or have urgent questions, please reply directly to this notification.

            Warm regards,
            Academic Career Counseling & Advising Center
        """.trimIndent()

        launchEmailClient(context, appointment.studentEmail, subject, body)
    }

    fun sendDeadlineEmail(
        context: Context,
        studentEmail: String,
        studentName: String,
        deadline: AssessmentDeadline
    ) {
        val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(deadline.dueDate))

        val subject = if (deadline.isUrgent) {
            "URGENT DEADLINE: ${deadline.title} Due on $formattedDate"
        } else {
            "Reminder: Upcoming Assessment Deadline - ${deadline.title}"
        }

        val body = """
            Dear $studentName,

            This is a reminder regarding an upcoming career counseling assessment deadline:

            • Assessment: ${deadline.title}
            • Target Group: ${deadline.targetAudience}
            • Final Submission Due Date: $formattedDate

            Details:
            ${deadline.description}

            Completing your career counseling assessment on time ensures you can schedule your individual advising session and receive your personalized career matching report.

            Best regards,
            Academic Career Counseling Office
        """.trimIndent()

        launchEmailClient(context, studentEmail, subject, body)
    }

    private fun launchEmailClient(
        context: Context,
        recipient: String,
        subject: String,
        body: String
    ) {
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${Uri.encode(recipient)}")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(emailIntent)
            Toast.makeText(context, "Opening email notification for $recipient...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "No email app found. Notification logged in app.", Toast.LENGTH_LONG).show()
        }
    }
}
