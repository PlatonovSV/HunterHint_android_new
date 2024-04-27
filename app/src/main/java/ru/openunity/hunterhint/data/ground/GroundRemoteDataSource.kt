package ru.openunity.hunterhint.data.ground

import ru.openunity.hunterhint.network.GroundRetrofitService
import javax.inject.Inject

class GroundRemoteDataSource @Inject constructor(groundRetrofitService: GroundRetrofitService) :
    GroundRetrofitService by groundRetrofitService {

}
