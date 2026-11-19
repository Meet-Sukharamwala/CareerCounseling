package com.example.data.model

enum class UserRole(val displayName: String, val badgeColorHex: Long) {
    STUDENT("Student", 0xFF2563EB),
    ADMIN("Faculty Counselor", 0xFF7C3AED)
}

enum class EducationLevel(val displayName: String, val category: String) {
    HIGH_SCHOOL_FRESHMAN("Secondary / High School Freshman (10th)", "High School"),
    HIGH_SCHOOL_JUNIOR("High School Junior", "High School"),
    HIGH_SCHOOL_SENIOR("High School Senior", "High School"),
    COLLEGE_UNDERGRAD("College Undergraduate", "Higher Education"),
    COLLEGE_GRAD("College Graduate / Early Career", "Higher Education")
}
