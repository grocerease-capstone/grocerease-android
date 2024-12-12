package com.exal.grocerease.helper.manager

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val EMAIL_KEY = "email_token"
    }

    fun saveEmail(token: String) {
        sharedPreferences.edit().putString(EMAIL_KEY, token).apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(EMAIL_KEY, null)
    }

    fun clearEmail() {
        sharedPreferences.edit().remove(EMAIL_KEY).apply()
    }
}