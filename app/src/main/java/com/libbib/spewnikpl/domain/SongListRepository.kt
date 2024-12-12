package com.libbib.spewnikpl.domain

import kotlinx.coroutines.flow.Flow

interface SongListRepository {
    fun getSongList(): Flow<List<Song>>

    fun getSong(id: Int): Flow<Song>

}