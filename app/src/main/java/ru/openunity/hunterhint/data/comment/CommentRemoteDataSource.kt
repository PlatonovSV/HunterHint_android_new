package ru.openunity.hunterhint.data.comment

import ru.openunity.hunterhint.network.CommentRetrofitService
import javax.inject.Inject

class CommentRemoteDataSource @Inject constructor(retrofitService: CommentRetrofitService) :
    CommentRetrofitService by retrofitService