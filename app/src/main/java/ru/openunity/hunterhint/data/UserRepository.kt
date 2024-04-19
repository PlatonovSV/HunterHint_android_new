package ru.openunity.hunterhint.data

import ru.openunity.hunterhint.network.UserRetrofitService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRetrofitService by remoteDataSource, UserDao by localDataSource {

}
