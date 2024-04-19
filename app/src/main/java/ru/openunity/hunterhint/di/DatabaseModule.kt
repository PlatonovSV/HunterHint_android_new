package ru.openunity.hunterhint.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.openunity.hunterhint.data.AppDatabase

// @Module informs Dagger that this class is a Dagger Module
@Module
class DatabaseModule {
    @Provides
    fun provideAppDatabase(context: Context) = AppDatabase.getDatabase(context)

    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()
}