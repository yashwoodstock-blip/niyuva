package com.niyuva.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notifications_config",
    indices = [Index(value = ["type"], unique = true)]
)
data class NotificationConfigEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "enabled", defaultValue = "1")
    val enabled: Boolean = true,

    @ColumnInfo(name = "days_before")
    val daysBefore: Int? = null,

    @ColumnInfo(name = "time_of_day")
    val timeOfDay: String? = null,

    @ColumnInfo(name = "custom_message")
    val customMessage: String? = null
)
