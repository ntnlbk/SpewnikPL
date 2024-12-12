package com.libbib.spewnikpl.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SongDbModel::class], version = 1, exportSchema = true)
abstract class SongRoomDatabase: RoomDatabase() {
    abstract fun songListDao(): SongListDao

    companion object {
        @Volatile
        private var INSTANCE: SongRoomDatabase? = null

        fun getDatabase(context: Context): SongRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongRoomDatabase::class.java,
                    "word_database"
                )
                    .createFromAsset("songs.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}