package ru.openunity.hunterhint.data

import ru.openunity.hunterhint.network.UserRemoteDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRemoteDataSource by remoteDataSource {
}
