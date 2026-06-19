package com.niyuva.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.niyuva.app.data.local.dao.ChatLogDao
import com.niyuva.app.data.local.dao.CycleDao
import com.niyuva.app.data.local.dao.DailyLogDao
import com.niyuva.app.data.local.dao.InsightDao
import com.niyuva.app.data.local.dao.NotificationConfigDao
import com.niyuva.app.data.local.dao.UserProfileDao
import com.niyuva.app.data.local.entity.ChatLogEntity
import com.niyuva.app.data.local.entity.CycleEntity
import com.niyuva.app.data.local.entity.DailyLogEntity
import com.niyuva.app.data.local.entity.InsightEntity
import com.niyuva.app.data.local.entity.NotificationConfigEntity
import com.niyuva.app.data.local.entity.UserProfileEntity

import com.niyuva.app.data.local.dao.StreakDao
import com.niyuva.app.data.local.dao.AiAnalysisResultDao
import com.niyuva.app.data.local.entity.StreakEntity
import com.niyuva.app.data.local.entity.AiAnalysisResultEntity
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        CycleEntity::class,
        DailyLogEntity::class,
        ChatLogEntity::class,
        InsightEntity::class,
        UserProfileEntity::class,
        NotificationConfigEntity::class,
        StreakEntity::class,
        AiAnalysisResultEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class NiyuvaDatabase : RoomDatabase() {
    abstract fun cycleDao(): CycleDao
    abstract fun dailyLogDao(): DailyLogDao
    abstract fun chatLogDao(): ChatLogDao
    abstract fun insightDao(): InsightDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun notificationConfigDao(): NotificationConfigDao
    abstract fun streakDao(): StreakDao
    abstract fun aiAnalysisResultDao(): AiAnalysisResultDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `streaks` (
                        `id` INTEGER NOT NULL,
                        `current_streak` INTEGER NOT NULL DEFAULT 0,
                        `longest_streak` INTEGER NOT NULL DEFAULT 0,
                        `last_log_date` TEXT,
                        `milestone_7_shown` INTEGER NOT NULL DEFAULT 0,
                        `milestone_30_shown` INTEGER NOT NULL DEFAULT 0,
                        `milestone_100_shown` INTEGER NOT NULL DEFAULT 0,
                        `updated_at` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `ai_analysis_results` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `analysis_date` TEXT NOT NULL,
                        `analysis_type` TEXT NOT NULL,
                        `chunk_sequence` INTEGER NOT NULL,
                        `raw_response` TEXT NOT NULL,
                        `parsed_summary` TEXT NOT NULL,
                        `status` TEXT NOT NULL,
                        `created_at` TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }
    }
}
