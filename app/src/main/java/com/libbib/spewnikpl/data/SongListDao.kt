package com.libbib.spewnikpl.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SongListDao {

    @Query("SELECT * FROM songs_table")
    fun getSongList(): Flow<List<SongDbModel>>

    @Query("SELECT * FROM songs_table WHERE id=:songId LIMIT 1")
    fun getSong(songId: Int): Flow<SongDbModel>

}