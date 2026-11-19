package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AssessmentDeadline
import com.example.data.model.CounselingAppointment
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments ORDER BY timestamp ASC")
    fun getAllAppointments(): Flow<List<CounselingAppointment>>

    @Query("SELECT * FROM appointments WHERE studentId = :studentId ORDER BY timestamp ASC")
    fun getAppointmentsForStudent(studentId: Long): Flow<List<CounselingAppointment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: CounselingAppointment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<CounselingAppointment>)

    @Update
    suspend fun updateAppointment(appointment: CounselingAppointment)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateAppointmentStatus(id: Long, status: String)

    @Query("UPDATE appointments SET reminderEmailSent = 1 WHERE id = :id")
    suspend fun markReminderSent(id: Long)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteAppointment(id: Long)

    // Deadlines
    @Query("SELECT * FROM assessment_deadlines ORDER BY dueDate ASC")
    fun getAllDeadlines(): Flow<List<AssessmentDeadline>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeadline(deadline: AssessmentDeadline): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeadlines(deadlines: List<AssessmentDeadline>)

    @Update
    suspend fun updateDeadline(deadline: AssessmentDeadline)

    @Query("UPDATE assessment_deadlines SET reminderDispatched = 1 WHERE id = :id")
    suspend fun markDeadlineNotified(id: Long)

    @Query("DELETE FROM assessment_deadlines WHERE id = :id")
    suspend fun deleteDeadline(id: Long)
}
