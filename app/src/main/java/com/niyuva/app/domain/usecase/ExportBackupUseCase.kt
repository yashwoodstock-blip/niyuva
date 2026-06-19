package com.niyuva.app.domain.usecase

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.niyuva.app.data.local.AesEncryptor
import com.niyuva.app.data.local.preferences.NiyuvaPreferences
import com.niyuva.app.domain.model.BackupData
import com.niyuva.app.domain.repository.ChatRepository
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.DailyLogRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject

class ExportBackupUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cycleRepository: CycleRepository,
    private val dailyLogRepository: DailyLogRepository,
    private val userProfileRepository: UserProfileRepository,
    private val chatRepository: ChatRepository,
    private val preferences: NiyuvaPreferences
) {
    suspend operator fun invoke(outputUri: Uri): Boolean {
        return try {
            // 1. Build a JSON snapshot of all data
            val backup = BackupData(
                version = 1,
                exportedAt = LocalDateTime.now().toString(),
                profile = userProfileRepository.getProfile(),
                cycles = cycleRepository.getRecentCycles(9999),
                recentLogs = dailyLogRepository.getRecentLogs(9999),
                messages = chatRepository.getRecentMessages(9999)
            )
            val json = Gson().toJson(backup)

            // 2. Encrypt with AES-256 using the db encryption key
            val key = preferences.getString(NiyuvaPreferences.Keys.KEY_DB_ENCRYPTION) ?: return false
            val encrypted = AesEncryptor.encrypt(json.toByteArray(Charsets.UTF_8), key)

            // 3. Write to the URI via ContentResolver
            context.contentResolver.openOutputStream(outputUri)?.use { it.write(encrypted) }
            true
        } catch (e: Exception) {
            false
        }
    }
}
