package com.example.school2120app

import android.app.Application
import com.example.school2120app.core.util.Prefs
import com.example.school2120app.presentation.contacts.ContactsFragment
import com.yandex.mapkit.MapKitFactory
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
        MapKitFactory.setApiKey(ContactsFragment.YANDEX_MAP_API_KEY)
    }
}