package ru.openunity.hunterhint.data.user

import ru.openunity.hunterhint.network.UserRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRetrofitService by remoteDataSource, UserDao by localDataSource {

}
