package com.niyuva.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.niyuva.app.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfile(): UserProfileEntity?

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeProfile(): Flow<UserProfileEntity?>

    @Query("UPDATE user_profile SET name = :name WHERE id = 1")
    suspend fun updateName(name: String)

    @Query("UPDATE user_profile SET age = :age WHERE id = 1")
    suspend fun updateAge(age: Int)

    @Query("UPDATE user_profile SET pin_hash = :pinHash WHERE id = 1")
    suspend fun updatePinHash(pinHash: String?)

    @Query("UPDATE user_profile SET security_question = :question, security_answer_hash = :answerHash WHERE id = 1")
    suspend fun updateSecurityQuestion(question: String, answerHash: String)

    @Query("UPDATE user_profile SET ai_enabled = :enabled, ai_provider = :provider WHERE id = 1")
    suspend fun updateAiSettings(enabled: Int, provider: String?)

    @Query("UPDATE user_profile SET onboarding_complete = 1 WHERE id = 1")
    suspend fun markOnboardingComplete()

    @Query("UPDATE user_profile SET average_cycle_length = :cycleLen, average_period_length = :periodLen WHERE id = 1")
    suspend fun updateAverages(cycleLen: Int?, periodLen: Int?)
}
