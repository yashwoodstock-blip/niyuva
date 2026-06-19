package com.niyuva.app.domain.usecase

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.niyuva.app.data.local.AesEncryptor
import com.niyuva.app.data.local.dao.ChatLogDao
import com.niyuva.app.data.local.dao.CycleDao
import com.niyuva.app.data.local.dao.DailyLogDao
import com.niyuva.app.data.local.dao.InsightDao
import com.niyuva.app.data.local.dao.NotificationConfigDao
import com.niyuva.app.data.local.dao.UserProfileDao
import com.niyuva.app.data.local.preferences.NiyuvaPreferences
import com.niyuva.app.domain.model.BackupData
import com.niyuva.app.domain.repository.ChatRepository
import com.niyuva.app.domain.repository.CycleRepository
import com.niyuva.app.domain.repository.DailyLogRepository
import com.niyuva.app.domain.repository.UserProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

sealed class RestoreResult {
    object Success : RestoreResult()
    data class Error(val msg: String) : RestoreResult()
    object WaitingForConfirmation : RestoreResult()
}

class RestoreBackupUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cycleDao: CycleDao,
    private val dailyLogDao: DailyLogDao,
    private val chatLogDao: ChatLogDao,
    private val insightDao: InsightDao,
    private val notificationConfigDao: NotificationConfigDao,
    private val userProfileDao: UserProfileDao,
    private val cycleRepository: CycleRepository,
    private val dailyLogRepository: DailyLogRepository,
    private val chatRepository: ChatRepository,
    private val userProfileRepository: UserProfileRepository,
    private val preferences: NiyuvaPreferences
) {
    suspend operator fun invoke(inputUri: Uri): RestoreResult {
        return try {
            val backupData = readAndDecrypt(inputUri)
            if (backupData.version != 1) {
                return RestoreResult.Error("Yeh backup compatible nahi hai aur version ke saath 💛")
            }
            RestoreResult.WaitingForConfirmation
        } catch (e: Exception) {
            RestoreResult.Error(e.localizedMessage ?: "Restore failed")
        }
    }

    suspend fun confirmRestore(inputUri: Uri): Boolean {
        return try {
            val backupData = readAndDecrypt(inputUri)
            
            // Clear existing tables
            cycleDao.deleteAllCycles()
            dailyLogDao.deleteAllLogs()
            chatLogDao.deleteAllMessages()
            insightDao.deleteAllInsights()
            notificationConfigDao.deleteAll()
            
            // Save restore components
            backupData.profile?.let { profile ->
                userProfileRepository.saveProfile(profile)
            }
            backupData.cycles.forEach { cycle ->
                cycleRepository.saveCycle(cycle)
            }
            backupData.recentLogs.forEach { log ->
                dailyLogRepository.saveLog(log)
            }
            backupData.messages.forEach { msg ->
                chatRepository.saveMessage(msg)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun readAndDecrypt(uri: Uri): BackupData {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Cannot open backup file")
        val bytes = inputStream.use { it.readBytes() }
        
        val key = preferences.getString(NiyuvaPreferences.Keys.KEY_DB_ENCRYPTION)
            ?: throw Exception("Encryption key missing")
            
        val decryptedBytes = AesEncryptor.decrypt(bytes, key)
        val json = String(decryptedBytes, Charsets.UTF_8)
        return Gson().fromJson(json, BackupData::class.java)
    }
}
