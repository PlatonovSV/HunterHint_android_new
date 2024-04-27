package ru.openunity.hunterhint

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HunterHintApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
