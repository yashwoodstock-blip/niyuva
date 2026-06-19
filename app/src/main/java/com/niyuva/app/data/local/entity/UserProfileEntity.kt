package com.niyuva.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "age")
    val age: Int? = null,

    @ColumnInfo(name = "average_cycle_length")
    val averageCycleLength: Int? = null,

    @ColumnInfo(name = "average_period_length")
    val averagePeriodLength: Int? = null,

    @ColumnInfo(name = "last_period_start_date")
    val lastPeriodStartDate: LocalDate? = null,

    @ColumnInfo(name = "pin_hash")
    val pinHash: String? = null,

    @ColumnInfo(name = "security_question")
    val securityQuestion: String? = null,

    @ColumnInfo(name = "security_answer_hash")
    val securityAnswerHash: String? = null,

    @ColumnInfo(name = "ai_enabled", defaultValue = "0")
    val aiEnabled: Boolean = false,

    @ColumnInfo(name = "ai_provider")
    val aiProvider: String? = null,

    @ColumnInfo(name = "onboarding_complete", defaultValue = "0")
    val onboardingComplete: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime
)
