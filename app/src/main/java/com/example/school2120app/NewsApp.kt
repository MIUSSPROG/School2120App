package com.example.school2120app

import android.app.Application
import com.example.school2120app.core.util.Prefs
import dagger.hilt.android.HiltAndroidApp


val prefs by lazy {
    Prefs(NewsApp.instance)
}

@HiltAndroidApp
class NewsApp: Application(){

    companion object {
        lateinit var instance: NewsApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}