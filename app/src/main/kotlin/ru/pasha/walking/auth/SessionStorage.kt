package ru.pasha.walking.auth

import android.content.SharedPreferences
import ru.pasha.common.di.ApplicationScope
import javax.inject.Inject

interface SessionStorage {
    fun saveSessionKey(key: String)
    fun getSessionKey(): String?
    fun clearSession()
}

@ApplicationScope
class EncryptedSessionStorage @Inject constructor(
    private val prefs: SharedPreferences
) : SessionStorage {

    override fun saveSessionKey(key: String) {
        prefs.edit().putString(SESSION_KEY, key).apply()
    }

    override fun getSessionKey(): String? {
        return prefs.getString(SESSION_KEY, null)
    }

    override fun clearSession() {
        prefs.edit().remove(SESSION_KEY).apply()
    }

    companion object {
        private const val SESSION_KEY = "encrypted_session_key"
    }
}
