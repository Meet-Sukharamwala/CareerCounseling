package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AssessmentDeadline
import com.example.data.model.AssessmentQuestion
import com.example.data.model.AssessmentResult
import com.example.data.model.CareerPath
import com.example.data.model.CounselingAppointment
import com.example.data.model.EducationLevel
import com.example.data.model.PersonalityTrait
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.data.repository.CareerCounselingRepository
import com.example.data.repository.CloudSyncStatus
import com.example.util.EmailNotificationHelper
import com.example.util.PdfReportGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CareerCounselingViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = CareerCounselingRepository(application, database)

    val allProfiles: StateFlow<List<UserProfile>> = repository.allProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCareers: StateFlow<List<CareerPath>> = repository.allCareers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allQuestions: StateFlow<List<AssessmentQuestion>> = repository.allQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeQuestions: StateFlow<List<AssessmentQuestion>> = repository.activeQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allResults: StateFlow<List<AssessmentResult>> = repository.allResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAppointments: StateFlow<List<CounselingAppointment>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDeadlines: StateFlow<List<AssessmentDeadline>> = repository.allDeadlines
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently Active Profile (Student or Faculty)
    private val _currentProfile = MutableStateFlow<UserProfile?>(null)
    val currentProfile: StateFlow<UserProfile?> = _currentProfile.asStateFlow()

    // Test Taking State
    private val _testAnswers = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val testAnswers: StateFlow<Map<Long, Int>> = _testAnswers.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _isSubmittingTest = MutableStateFlow(false)
    val isSubmittingTest: StateFlow<Boolean> = _isSubmittingTest.asStateFlow()

    // Active Result being inspected in detail
    private val _viewingResult = MutableStateFlow<AssessmentResult?>(null)
    val viewingResult: StateFlow<AssessmentResult?> = _viewingResult.asStateFlow()

    // Cloud Sync State
    private val _cloudSyncStatus = MutableStateFlow(CloudSyncStatus())
    val cloudSyncStatus: StateFlow<CloudSyncStatus> = _cloudSyncStatus.asStateFlow()

    // Admin Roster Search / Filter
    private val _rosterSearchQuery = MutableStateFlow("")
    val rosterSearchQuery: StateFlow<String> = _rosterSearchQuery.asStateFlow()

    private val _rosterFilterLevel = MutableStateFlow<EducationLevel?>(null)
    val rosterFilterLevel: StateFlow<EducationLevel?> = _rosterFilterLevel.asStateFlow()

    // Filtered Student Results for the current student
    val currentStudentResults: StateFlow<List<AssessmentResult>> = combine(
        allResults,
        _currentProfile
    ) { results, profile ->
        if (profile == null || profile.role == UserRole.ADMIN) {
            results
        } else {
            results.filter { it.studentId == profile.id }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Appointments for current user
    val currentUserAppointments: StateFlow<List<CounselingAppointment>> = combine(
        allAppointments,
        _currentProfile
    ) { appointments, profile ->
        if (profile == null || profile.role == UserRole.ADMIN) {
            appointments
        } else {
            appointments.filter { it.studentId == profile.id }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
            // Auto-select first profile (defaults to Jordan Miller, high school senior)
            allProfiles.collect { profiles ->
                if (_currentProfile.value == null && profiles.isNotEmpty()) {
                    // Default to Jordan Miller or first student
                    val defaultStudent = profiles.find { it.role == UserRole.STUDENT } ?: profiles.first()
                    _currentProfile.value = defaultStudent
                }
            }
        }
    }

    fun switchProfile(profile: UserProfile) {
        _currentProfile.value = profile
        _viewingResult.value = null
    }

    fun createStudentProfile(
        name: String,
        email: String,
        level: EducationLevel,
        major: String,
        targetIndustries: String
    ) {
        viewModelScope.launch {
            val newProfile = UserProfile(
                name = name.trim(),
                email = email.trim(),
                role = UserRole.STUDENT,
                educationLevel = level,
                academicMajor = major.trim().ifBlank { "Undeclared" },
                targetIndustries = targetIndustries.trim().ifBlank { "General STEM & Arts" },
                avatarColorHex = 0xFF0284C7
            )
            val newId = repository.insertProfile(newProfile)
            _currentProfile.value = newProfile.copy(id = newId)
        }
    }

    // Assessment Actions
    fun startNewAssessment() {
        _testAnswers.value = emptyMap()
        _currentQuestionIndex.value = 0
        _isSubmittingTest.value = false
    }

    fun answerQuestion(questionId: Long, rating: Int) {
        val updated = _testAnswers.value.toMutableMap()
        updated[questionId] = rating
        _testAnswers.value = updated
    }

    fun goToNextQuestion(totalQuestions: Int) {
        if (_currentQuestionIndex.value < totalQuestions - 1) {
            _currentQuestionIndex.value += 1
        }
    }

    fun goToPreviousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    fun submitAssessment(onComplete: (AssessmentResult) -> Unit) {
        val profile = _currentProfile.value ?: return
        viewModelScope.launch {
            _isSubmittingTest.value = true
            val result = repository.submitAssessment(profile, _testAnswers.value)
            _isSubmittingTest.value = false
            _viewingResult.value = result
            onComplete(result)
        }
    }

    fun setViewingResult(result: AssessmentResult?) {
        _viewingResult.value = result
    }

    fun getMatchedCareersForResult(result: AssessmentResult): List<CareerPath> {
        val careers = allCareers.value
        return careers.map { career ->
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
            val match = (pScore * 0.65f + sScore * 0.35f).toInt()
            career to match
        }.sortedByDescending { it.second }.map { it.first }
    }

    // PDF Report Export
    fun exportAndSharePdf(context: Context, result: AssessmentResult) {
        viewModelScope.launch {
            val matchedCareers = getMatchedCareersForResult(result)
            val file = PdfReportGenerator.generateReportPdf(context, result, matchedCareers)
            if (file != null) {
                PdfReportGenerator.openOrSharePdf(context, file, "Career Assessment Report - ${result.studentName}")
            } else {
                Toast.makeText(context, "Failed to compile assessment PDF report", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Counselor Review
    fun addCounselorNotes(resultId: Long, notes: String) {
        val profile = _currentProfile.value
        val counselorName = if (profile?.role == UserRole.ADMIN) profile.name else "Dr. Sarah Jenkins"
        viewModelScope.launch {
            repository.addCounselorReview(resultId, notes, counselorName)
            val current = _viewingResult.value
            if (current != null && current.id == resultId) {
                _viewingResult.value = current.copy(
                    counselorNotes = notes,
                    counselorReviewed = true,
                    counselorReviewedBy = counselorName,
                    counselorReviewedTimestamp = System.currentTimeMillis()
                )
            }
        }
    }

    // Content Management (Admin)
    fun saveCareer(career: CareerPath) {
        viewModelScope.launch {
            repository.saveCareer(career)
        }
    }

    fun deleteCareer(id: Long) {
        viewModelScope.launch {
            repository.deleteCareer(id)
        }
    }

    fun saveQuestion(question: AssessmentQuestion) {
        viewModelScope.launch {
            repository.saveQuestion(question)
        }
    }

    fun deleteQuestion(id: Long) {
        viewModelScope.launch {
            repository.deleteQuestion(id)
        }
    }

    // Appointments & Email Notifications
    fun scheduleAppointment(
        appointmentType: String,
        timestamp: Long,
        location: String,
        notes: String
    ) {
        val profile = _currentProfile.value ?: return
        viewModelScope.launch {
            val newAppointment = CounselingAppointment(
                studentId = profile.id,
                studentName = profile.name,
                studentEmail = profile.email,
                counselorName = "Dr. Sarah Jenkins",
                appointmentType = appointmentType,
                timestamp = timestamp,
                location = location,
                notes = notes
            )
            repository.scheduleAppointment(newAppointment)
        }
    }

    fun updateAppointmentStatus(id: Long, status: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(id, status)
        }
    }

    fun sendAppointmentEmailReminder(context: Context, appointment: CounselingAppointment) {
        viewModelScope.launch {
            EmailNotificationHelper.sendAppointmentEmail(context, appointment, isReminder = true)
            repository.markAppointmentReminderSent(appointment.id)
        }
    }

    fun sendDeadlineEmailReminder(
        context: Context,
        deadline: AssessmentDeadline,
        studentEmail: String,
        studentName: String
    ) {
        viewModelScope.launch {
            EmailNotificationHelper.sendDeadlineEmail(context, studentEmail, studentName, deadline)
            repository.markDeadlineNotified(deadline.id)
        }
    }

    fun saveDeadline(deadline: AssessmentDeadline) {
        viewModelScope.launch {
            repository.saveDeadline(deadline)
        }
    }

    fun deleteDeadline(id: Long) {
        viewModelScope.launch {
            repository.deleteDeadline(id)
        }
    }

    // Cloud Storage Sync
    fun performCloudSync() {
        viewModelScope.launch {
            _cloudSyncStatus.value = _cloudSyncStatus.value.copy(isSyncing = true)
            val updatedStatus = repository.cloudStorageManager.performCloudSync()
            _cloudSyncStatus.value = updatedStatus
        }
    }

    fun exportCloudBackup(context: Context) {
        viewModelScope.launch {
            val file = repository.cloudStorageManager.exportDepartmentCloudBackup()
            if (file != null) {
                repository.cloudStorageManager.shareCloudBackup(context, file)
            } else {
                Toast.makeText(context, "Could not generate cloud backup package", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Roster Filtering
    fun setRosterSearch(query: String) {
        _rosterSearchQuery.value = query
    }

    fun setRosterFilterLevel(level: EducationLevel?) {
        _rosterFilterLevel.value = level
    }
}
