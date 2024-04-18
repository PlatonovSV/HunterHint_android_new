package ru.openunity.hunterhint.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.openunity.hunterhint.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)
}
