package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AssessmentQuestion
import kotlinx.coroutines.flow.Flow

@Dao
interface AssessmentQuestionDao {
    @Query("SELECT * FROM assessment_questions ORDER BY sortOrder ASC")
    fun getAllQuestions(): Flow<List<AssessmentQuestion>>

    @Query("SELECT * FROM assessment_questions WHERE isActive = 1 ORDER BY sortOrder ASC")
    fun getActiveQuestions(): Flow<List<AssessmentQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<AssessmentQuestion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: AssessmentQuestion): Long

    @Update
    suspend fun updateQuestion(question: AssessmentQuestion)

    @Query("DELETE FROM assessment_questions WHERE id = :id")
    suspend fun deleteQuestion(id: Long)

    @Query("SELECT COUNT(*) FROM assessment_questions")
    suspend fun getCount(): Int
}
