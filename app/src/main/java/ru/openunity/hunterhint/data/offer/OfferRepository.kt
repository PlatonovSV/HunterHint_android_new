package ru.openunity.hunterhint.data.offer

import ru.openunity.hunterhint.network.OfferRetrofitService
import javax.inject.Inject

class OfferRepository @Inject constructor(remoteDataSource: OfferRemoteDataSource) :
    OfferRetrofitService by remoteDataSource {

}