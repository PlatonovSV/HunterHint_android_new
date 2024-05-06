package ru.openunity.hunterhint.data.offer

import ru.openunity.hunterhint.network.OfferRetrofitService
import javax.inject.Inject

class OfferRemoteDataSource @Inject constructor(retrofitService: OfferRetrofitService) :
    OfferRetrofitService by retrofitService {

}