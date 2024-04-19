package ru.openunity.hunterhint

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.openunity.hunterhint.data.UserRepository
import ru.openunity.hunterhint.di.DatabaseModule
import ru.openunity.hunterhint.di.NetworkModule
import ru.openunity.hunterhint.network.GroundRemoteDataSource
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface ApplicationComponent {
    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
    fun getGroundRemoteDataSource(): GroundRemoteDataSource
    fun getUserRepository(): UserRepository
}

class HunterHintApplication : Application() {
    // Reference to the application graph that is used across the whole app
    val appComponent: ApplicationComponent = DaggerApplicationComponent
        .factory().create(this)
}
