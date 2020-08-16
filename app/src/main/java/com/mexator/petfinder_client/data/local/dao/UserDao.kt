package com.mexator.petfinder_client.data.local.dao

import androidx.room.*
import com.mexator.petfinder_client.data.local.entity.FavoriteEntity
import com.mexator.petfinder_client.data.local.entity.PetEntity
import com.mexator.petfinder_client.data.model.User
import com.mexator.petfinder_client.data.remote.pojo.Favorite
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("select * from User")
    fun getUser(): Single<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User)

    @Query("select * from pets p join FavoriteEntity fav on p.id == fav.id")
    fun getFavorites(): Single<List<PetEntity>>

    @Insert
    fun saveFavorites(list: List<FavoriteEntity>)

    @Query("delete from FavoriteEntity")
    fun clearFavorites()


    @Query("delete from User")
    fun deleteCurrentUser()
}