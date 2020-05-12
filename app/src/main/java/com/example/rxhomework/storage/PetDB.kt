package com.example.rxhomework.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PetEntity::class, PhotoEntity::class], version = DB_VERSION)
abstract class PetDB : RoomDatabase() {
    abstract fun petDao(): PetDao

    companion object {
        @Volatile
        private var databseInstance: PetDB? = null

        fun getDatabasenIstance(mContext: Context): PetDB =
            databseInstance ?: synchronized(this) {
                databseInstance ?: buildDatabaseInstance(mContext).also {
                    databseInstance = it
                }
            }

        private fun buildDatabaseInstance(mContext: Context) =
            Room.databaseBuilder(mContext, PetDB::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}


const val DB_VERSION = 1

const val DB_NAME = "PersonDataSample.db"
