package ru.openunity.hunterhint.data.user

import ru.openunity.hunterhint.network.UserRetrofitService
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(userRetrofitService: UserRetrofitService) :
    UserRetrofitService by userRetrofitService {
}