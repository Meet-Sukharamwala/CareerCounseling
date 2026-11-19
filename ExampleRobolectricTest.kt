package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.EducationLevel
import com.example.data.model.PersonalityTrait
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Career Counseling", appName)
    }

    @Test
    fun `verify user profile creation and role separation`() {
        val student = UserProfile(
            name = "Jordan Miller",
            email = "jordan@school.edu",
            role = UserRole.STUDENT,
            educationLevel = EducationLevel.HIGH_SCHOOL_SENIOR,
            academicMajor = "Pre-Engineering"
        )
        val admin = UserProfile(
            name = "Dr. Sarah Jenkins",
            email = "sjenkins@school.edu",
            role = UserRole.ADMIN,
            educationLevel = EducationLevel.COLLEGE_GRAD,
            departmentalTitle = "Director of Career Advising"
        )

        assertEquals(UserRole.STUDENT, student.role)
        assertEquals(UserRole.ADMIN, admin.role)
        assertTrue(student.educationLevel == EducationLevel.HIGH_SCHOOL_SENIOR)
    }

    @Test
    fun `verify riasec personality trait codes`() {
        assertEquals("R", PersonalityTrait.REALISTIC.code)
        assertEquals("I", PersonalityTrait.INVESTIGATIVE.code)
        assertEquals("A", PersonalityTrait.ARTISTIC.code)
        assertEquals("S", PersonalityTrait.SOCIAL.code)
        assertEquals("E", PersonalityTrait.ENTERPRISING.code)
        assertEquals("C", PersonalityTrait.CONVENTIONAL.code)
    }
}

