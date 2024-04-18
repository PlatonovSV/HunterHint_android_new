package ru.openunity.hunterhint.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.openunity.hunterhint.models.User


@Dao
interface UserDao {
    @Transaction
    @Query("DELETE from users")
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("DELETE from users")
    suspend fun delete()

    //В таблице users одновременно может быть не более 1-й строки
    @Query("SELECT * from users LIMIT 1")
    fun getUser(): Flow<User>
}
