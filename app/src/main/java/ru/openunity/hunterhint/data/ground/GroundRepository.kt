package ru.openunity.hunterhint.data.ground

import ru.openunity.hunterhint.data.ground.GroundRemoteDataSource
import ru.openunity.hunterhint.network.GroundRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroundRepository @Inject constructor(remoteDataSource: GroundRemoteDataSource) : GroundRetrofitService by remoteDataSource {
}