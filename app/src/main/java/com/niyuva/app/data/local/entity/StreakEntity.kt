package com.niyuva.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streaks")
data class StreakEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1,

    @ColumnInfo(name = "current_streak")
    val currentStreak: Int = 0,

    @ColumnInfo(name = "longest_streak")
    val longestStreak: Int = 0,

    @ColumnInfo(name = "last_log_date")
    val lastLogDate: String? = null,

    @ColumnInfo(name = "milestone_7_shown")
    val milestone7Shown: Int = 0,

    @ColumnInfo(name = "milestone_30_shown")
    val milestone30Shown: Int = 0,

    @ColumnInfo(name = "milestone_100_shown")
    val milestone100Shown: Int = 0,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)
