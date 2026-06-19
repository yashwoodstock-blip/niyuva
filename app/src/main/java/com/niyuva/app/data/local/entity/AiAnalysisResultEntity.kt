package com.niyuva.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_analysis_results")
data class AiAnalysisResultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "analysis_date")
    val analysisDate: String,

    @ColumnInfo(name = "analysis_type")
    val analysisType: String,

    @ColumnInfo(name = "chunk_sequence")
    val chunkSequence: Int,

    @ColumnInfo(name = "raw_response")
    val rawResponse: String,

    @ColumnInfo(name = "parsed_summary")
    val parsedSummary: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String
)
