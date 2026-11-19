package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CareerPath
import com.example.data.model.PersonalityTrait
import kotlinx.coroutines.flow.Flow

@Dao
interface CareerPathDao {
    @Query("SELECT * FROM careers ORDER BY industry ASC, title ASC")
    fun getAllCareers(): Flow<List<CareerPath>>

    @Query("SELECT * FROM careers WHERE primaryTrait = :trait OR secondaryTrait = :trait")
    fun getCareersForTrait(trait: PersonalityTrait): Flow<List<CareerPath>>

    @Query("SELECT * FROM careers WHERE id = :id LIMIT 1")
    suspend fun getCareerById(id: Long): CareerPath?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareer(career: CareerPath): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareers(careers: List<CareerPath>)

    @Update
    suspend fun updateCareer(career: CareerPath)

    @Query("DELETE FROM careers WHERE id = :id")
    suspend fun deleteCareer(id: Long)

    @Query("SELECT COUNT(*) FROM careers")
    suspend fun getCount(): Int
}
