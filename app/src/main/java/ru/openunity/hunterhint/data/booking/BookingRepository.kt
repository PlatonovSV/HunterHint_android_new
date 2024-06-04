package ru.openunity.hunterhint.data.booking

import ru.openunity.hunterhint.network.BookingRetrofitService
import javax.inject.Inject

class BookingRepository @Inject constructor(remoteDataSource: BookingRemoteDataSource) :
    BookingRetrofitService by remoteDataSource