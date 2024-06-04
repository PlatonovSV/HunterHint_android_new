package ru.openunity.hunterhint.data.comment;

import ru.openunity.hunterhint.network.CommentRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(remoteDataSource: CommentRemoteDataSource) :
    CommentRetrofitService by remoteDataSource
