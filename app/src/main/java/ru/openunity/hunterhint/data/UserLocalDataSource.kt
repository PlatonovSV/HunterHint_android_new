package ru.openunity.hunterhint.data


import ru.openunity.hunterhint.models.User
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(private val userDao: UserDao) : UserDao by userDao {
    override suspend fun insert(user: User) {
        userDao.delete()
        userDao.insert(user)
    }
}
