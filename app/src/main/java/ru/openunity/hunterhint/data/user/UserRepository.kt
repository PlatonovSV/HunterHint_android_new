package ru.openunity.hunterhint.data.user

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import ru.openunity.hunterhint.network.UserRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
    private val applicationScope: CoroutineScope
) : UserRetrofitService by remoteDataSource, UserDao by localDataSource {
    val isAuthorized = getUser()
        //if user table is empty, I get nul
        .map {
            it != null && it.jwt.isNotBlank()
        }
        .shareIn(
            applicationScope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )
}
