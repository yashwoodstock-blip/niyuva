package com.niyuva.app.data.local.database

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseKeyManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_FILE = "niyuva_secure_prefs"
        private const val KEY_DB_ENCRYPTION = "db_encryption_key"
    }

    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getDatabasePassphrase(): ByteArray {
        val encodedKey = sharedPreferences.getString(KEY_DB_ENCRYPTION, null)
        return if (encodedKey != null) {
            Base64.decode(encodedKey, Base64.NO_WRAP)
        } else {
            val rawKey = ByteArray(32)
            SecureRandom().nextBytes(rawKey)
            val newEncodedKey = Base64.encodeToString(rawKey, Base64.NO_WRAP)
            sharedPreferences.edit()
                .putString(KEY_DB_ENCRYPTION, newEncodedKey)
                .apply()
            rawKey
        }
    }
}
