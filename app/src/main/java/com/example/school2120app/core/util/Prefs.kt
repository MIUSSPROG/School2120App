package com.example.school2120app.core.util

import android.content.Context
import androidx.core.content.edit

class Prefs(context: Context) {
    private val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var prefBuilding: String?
    get() = sharedPref.getString(KEY_BUILDING, null)
    set(value) = sharedPref.edit { putString(KEY_BUILDING, value) }

    var prefGrade: String?
        get() = sharedPref.getString(KEY_GRADE, null)
        set(value) = sharedPref.edit { putString(KEY_GRADE, value) }

    var prefLetter: String?
        get() = sharedPref.getString(KEY_LETTER, null)
        set(value) = sharedPref.edit { putString(KEY_LETTER, value) }

    var login: String?
        get() = sharedPref.getString(KEY_LOGIN, null)
        set(value) = sharedPref.edit { putString(KEY_LOGIN, value) }

    var password: String?
        get() = sharedPref.getString(KEY_PASSWORD, null)
        set(value) = sharedPref.edit { putString(KEY_PASSWORD, value) }

    companion object{
        private const val PREFS_NAME = "myPrefs"
        private const val KEY_BUILDING = "keyBuilding"
        private const val KEY_GRADE = "keyGrade"
        private const val KEY_LETTER = "keyLetter"
        private const val KEY_LOGIN = "keyLogin"
        private const val KEY_PASSWORD = "keyPassword"
    }
}