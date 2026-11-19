package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.EducationLevel
import com.example.data.model.PersonalityTrait
import com.example.data.model.UserRole

class Converters {
    @TypeConverter
    fun fromPersonalityTrait(trait: PersonalityTrait?): String? = trait?.name

    @TypeConverter
    fun toPersonalityTrait(value: String?): PersonalityTrait? {
        return value?.let {
            runCatching { PersonalityTrait.valueOf(it) }.getOrNull()
        }
    }

    @TypeConverter
    fun fromEducationLevel(level: EducationLevel?): String? = level?.name

    @TypeConverter
    fun toEducationLevel(value: String?): EducationLevel? {
        return value?.let {
            runCatching { EducationLevel.valueOf(it) }.getOrNull() ?: EducationLevel.COLLEGE_UNDERGRAD
        }
    }

    @TypeConverter
    fun fromUserRole(role: UserRole?): String? = role?.name

    @TypeConverter
    fun toUserRole(value: String?): UserRole? {
        return value?.let {
            runCatching { UserRole.valueOf(it) }.getOrNull() ?: UserRole.STUDENT
        }
    }
}
