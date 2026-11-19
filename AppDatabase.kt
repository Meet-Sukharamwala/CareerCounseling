package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.AssessmentDeadline
import com.example.data.model.AssessmentQuestion
import com.example.data.model.AssessmentResult
import com.example.data.model.CareerPath
import com.example.data.model.CounselingAppointment
import com.example.data.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        AssessmentResult::class,
        CareerPath::class,
        AssessmentQuestion::class,
        CounselingAppointment::class,
        AssessmentDeadline::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun assessmentResultDao(): AssessmentResultDao
    abstract fun careerPathDao(): CareerPathDao
    abstract fun assessmentQuestionDao(): AssessmentQuestionDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "career_counseling_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
