package com.mexator.petfinder_client.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mexator.petfinder_client.data.model.User
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("select * from User")
    fun getUser(): Single<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User)

    @Query("delete from User")
    fun deleteCurrentUser()
}