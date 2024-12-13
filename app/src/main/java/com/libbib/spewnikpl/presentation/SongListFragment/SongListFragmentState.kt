package com.libbib.spewnikpl.presentation.SongListFragment

import com.libbib.spewnikpl.domain.Song
import com.libbib.spewnikpl.domain.SongType


sealed class SongListFragmentState {
    data object Progress : SongListFragmentState()
    class Content(
        val songList: List<Song>,
        val currentSongType: SongType
    ) : SongListFragmentState()
}