package com.niyuva.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "cycles")
data class CycleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "start_date")
    val startDate: LocalDate,

    @ColumnInfo(name = "end_date")
    val endDate: LocalDate? = null,

    @ColumnInfo(name = "cycle_length")
    val cycleLength: Int? = null,

    @ColumnInfo(name = "period_length")
    val periodLength: Int? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime
)
