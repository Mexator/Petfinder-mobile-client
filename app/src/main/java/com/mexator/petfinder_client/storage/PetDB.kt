package com.mexator.petfinder_client.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PetEntity::class, PhotoEntity::class], version = DB_VERSION)
abstract class PetDB : RoomDatabase() {
    abstract fun petDao(): PetDao

    companion object {
        @Volatile
        private var databaseInstance: PetDB? = null

        fun getDatabaseInstance(mContext: Context): PetDB =
            databaseInstance ?: synchronized(this) {
                databaseInstance ?: buildDatabaseInstance(mContext).also {
                    databaseInstance = it
                }
            }

        private fun buildDatabaseInstance(mContext: Context) =
            Room.databaseBuilder(mContext, PetDB::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}


const val DB_VERSION = 4

const val DB_NAME = "Pets.db"
