package ru.openunity.hunterhint

import android.app.Application
import ru.openunity.hunterhint.data.AppContainer
import ru.openunity.hunterhint.data.DefaultAppContainer

class HunterHintApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
