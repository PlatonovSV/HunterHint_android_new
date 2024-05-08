package ru.openunity.hunterhint.data.booking

import ru.openunity.hunterhint.network.BookingRetrofitService
import javax.inject.Inject

class BookingRemoteDataSource @Inject constructor(retrofitService: BookingRetrofitService) :
    BookingRetrofitService by retrofitService {
}