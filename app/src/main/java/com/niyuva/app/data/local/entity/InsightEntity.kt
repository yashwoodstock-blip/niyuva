package com.niyuva.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "insights")
data class InsightEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "insight_type")
    val insightType: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "source")
    val source: String,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime
)
