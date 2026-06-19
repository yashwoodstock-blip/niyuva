package com.niyuva.app.di

import android.app.AlarmManager
import android.content.Context
import androidx.work.WorkManager
import com.google.gson.Gson
import com.niyuva.app.data.repository.ChatRepositoryImpl
import com.niyuva.app.data.repository.CycleRepositoryImpl
import com.niyuva.app.data.repository.DailyLogRepositoryImpl
import com.niyuva.app.data.repository.UserProfileRepositoryImpl
import com.niyuva.app.domain.repository.ChatRepository
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.DailyLogRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import com.niyuva.app.data.repository.NotificationRepositoryImpl
import com.niyuva.app.domain.repository.NotificationRepository
import com.niyuva.app.data.repository.InsightRepositoryImpl
import com.niyuva.app.domain.repository.InsightRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCycleRepository(
        impl: CycleRepositoryImpl
    ): CycleRepository

    @Binds
    @Singleton
    abstract fun bindDailyLogRepository(
        impl: DailyLogRepositoryImpl
    ): DailyLogRepository

    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(
        impl: UserProfileRepositoryImpl
    ): UserProfileRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindInsightRepository(
        impl: InsightRepositoryImpl
    ): InsightRepository

    @Binds
    @Singleton
    abstract fun bindStreakRepository(
        impl: com.niyuva.app.data.repository.StreakRepositoryImpl
    ): com.niyuva.app.domain.repository.StreakRepository

    @Binds
    @Singleton
    abstract fun bindAiAnalysisRepository(
        impl: com.niyuva.app.data.repository.AiAnalysisRepositoryImpl
    ): com.niyuva.app.domain.repository.AiAnalysisRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
}
