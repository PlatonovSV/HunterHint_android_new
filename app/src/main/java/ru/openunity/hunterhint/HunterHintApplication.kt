package ru.openunity.hunterhint

import android.app.Application
import dagger.Component
import dagger.Provides
import ru.openunity.hunterhint.data.UserRepository
import ru.openunity.hunterhint.di.NetworkModule
import ru.openunity.hunterhint.network.GroundRemoteDataSource
import ru.openunity.hunterhint.network.UserRemoteDataSource
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun getGroundRemoteDataSource(): GroundRemoteDataSource
    fun getUserRepository(): UserRepository
}

class HunterHintApplication : Application() {
    // Reference to the application graph that is used across the whole app
    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()
}
