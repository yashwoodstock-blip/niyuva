package com.niyuva.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "daily_logs",
    indices = [Index(value = ["date"], unique = true)]
)
data class DailyLogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "flow_level")
    val flowLevel: String? = null,

    @ColumnInfo(name = "blood_color")
    val bloodColor: String? = null,

    @ColumnInfo(name = "clot_size")
    val clotSize: String? = null,

    @ColumnInfo(name = "pain_level")
    val painLevel: String? = null,

    @ColumnInfo(name = "pain_types")
    val painTypes: List<String>? = null,

    @ColumnInfo(name = "discharge_type")
    val dischargeType: String? = null,

    @ColumnInfo(name = "energy_level")
    val energyLevel: String? = null,

    @ColumnInfo(name = "sleep_quality")
    val sleepQuality: String? = null,

    @ColumnInfo(name = "mood")
    val mood: List<String>? = null,

    @ColumnInfo(name = "sexual_activity")
    val sexualActivity: String? = null,

    @ColumnInfo(name = "birth_control")
    val birthControl: String? = null,

    @ColumnInfo(name = "source", defaultValue = "manual")
    val source: String = "manual",

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime
)
