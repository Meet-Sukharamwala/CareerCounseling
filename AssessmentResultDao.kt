package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AssessmentResult
import kotlinx.coroutines.flow.Flow

@Dao
interface AssessmentResultDao {
    @Query("SELECT * FROM assessment_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<AssessmentResult>>

    @Query("SELECT * FROM assessment_results WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getResultsForStudent(studentId: Long): Flow<List<AssessmentResult>>

    @Query("SELECT * FROM assessment_results WHERE id = :id LIMIT 1")
    suspend fun getResultById(id: Long): AssessmentResult?

    @Query("SELECT * FROM assessment_results WHERE studentId = :studentId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestResultForStudent(studentId: Long): Flow<AssessmentResult?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: AssessmentResult): Long

    @Update
    suspend fun updateResult(result: AssessmentResult)

    @Query("UPDATE assessment_results SET counselorNotes = :notes, counselorReviewed = 1, counselorReviewedBy = :counselorName, counselorReviewedTimestamp = :timestamp WHERE id = :id")
    suspend fun addCounselorReview(id: Long, notes: String, counselorName: String, timestamp: Long)

    @Query("UPDATE assessment_results SET pdfExportPath = :path WHERE id = :id")
    suspend fun updatePdfPath(id: Long, path: String)

    @Query("DELETE FROM assessment_results WHERE id = :id")
    suspend fun deleteResult(id: Long)

    @Query("SELECT COUNT(*) FROM assessment_results")
    suspend fun getTotalAssessmentsCount(): Int
}
