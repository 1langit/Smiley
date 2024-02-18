package com.example.smiley.utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_FILENAME = "authAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_UID = "uid"
        private const val KEY_EMAIL = "email"
        private const val KEY_ROLE = "role"

        @Volatile
        private var instance: PrefManager? = null

        fun getInstance(context: Context) : PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUid(uid: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_UID, uid)
        editor.apply()
    }

    fun saveEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, email)
        editor.apply()
    }

    fun saveRole(role: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROLE, role)
        editor.apply()
    }

    fun getUid(): String {
        return sharedPreferences.getString(KEY_UID, "") ?: ""
    }

    fun getEmail(): String {
        return sharedPreferences.getString(KEY_EMAIL, "") ?: ""
    }

    fun getRole(): String {
        return sharedPreferences.getString(KEY_ROLE, "") ?: ""
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}