package com.niyuva.app.di

import android.content.Context
import androidx.room.Room
import com.niyuva.app.data.local.database.DatabaseKeyManager
import com.niyuva.app.data.local.database.NiyuvaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        keyManager: DatabaseKeyManager
    ): NiyuvaDatabase {
        val passphrase = keyManager.getDatabasePassphrase()
        val factory = SupportFactory(passphrase)
        // SECURITY FIX: Clear key from memory immediately after passing to factory
        passphrase.fill(0)
        return Room.databaseBuilder(
            context,
            NiyuvaDatabase::class.java,
            "niyuva_db"
        )
        .openHelperFactory(factory)
        .addMigrations(NiyuvaDatabase.MIGRATION_1_2)
        .build()
    }

    @Provides
    @Singleton
    fun provideCycleDao(db: NiyuvaDatabase) = db.cycleDao()

    @Provides
    @Singleton
    fun provideDailyLogDao(db: NiyuvaDatabase) = db.dailyLogDao()

    @Provides
    @Singleton
    fun provideChatLogDao(db: NiyuvaDatabase) = db.chatLogDao()

    @Provides
    @Singleton
    fun provideInsightDao(db: NiyuvaDatabase) = db.insightDao()

    @Provides
    @Singleton
    fun provideUserProfileDao(db: NiyuvaDatabase) = db.userProfileDao()

    @Provides
    @Singleton
    fun provideNotificationConfigDao(db: NiyuvaDatabase) = db.notificationConfigDao()

    @Provides
    @Singleton
    fun provideStreakDao(db: NiyuvaDatabase) = db.streakDao()

    @Provides
    @Singleton
    fun provideAiAnalysisResultDao(db: NiyuvaDatabase) = db.aiAnalysisResultDao()
}
