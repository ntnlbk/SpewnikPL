package com.libbib.spewnikpl.presentation.SongListFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libbib.spewnikpl.R
import com.libbib.spewnikpl.domain.GetSongListUseCase
import com.libbib.spewnikpl.domain.Song
import com.libbib.spewnikpl.domain.SongType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SongListViewModel @Inject constructor(
    private val getSongListUseCase: GetSongListUseCase,
    private val context: Application,
) : ViewModel() {


    private val _state = MutableStateFlow<SongListFragmentState>(SongListFragmentState.Progress)
    val state = _state.asStateFlow()
    private lateinit var actualList: List<Song>

    private var stringToSearch = ""
    private var typeToSort = SongType.ALL


    init {
        viewModelScope.launch {
            getSongListUseCase().collect {
                actualList = it
                _state.value = SongListFragmentState.Content(it, typeToSort)
            }
        }
    }

    private fun sortAndSearchInList() {
        if (this::actualList.isInitialized) {
            val sortedListByName = actualList.filter { song ->
                song.name.lowercase().contains(stringToSearch.lowercase())
            }.filter { song ->
                song.types.contains(typeToSort)
            }
            val sortedListByText =
                if (stringToSearch.length > MIN_CHAR_TO_SEARCH_TEXT) actualList.filter { song ->
                    song.text.lowercase().contains(stringToSearch.lowercase())
                }.filter { song ->
                    song.types.contains(typeToSort)
                } else ArrayList<Song>()
            val sortedList = sortedListByName + sortedListByText
            _state.value = SongListFragmentState.Content(sortedList, typeToSort)
        }
    }


    fun searchInList(chars: String) {
        stringToSearch = chars
        sortAndSearchInList()
    }

    fun sortSongListByType(stringType: String) {
        val type = when (stringType) {
            context.getString(R.string.all) -> SongType.ALL
            context.getString(R.string.parts) -> SongType.PART_OF_MASS
            context.getString(R.string.short_songs) -> SongType.SHORT
            context.getString(R.string.long_songs) -> SongType.LONG
            context.getString(R.string.carols) -> SongType.CAROLS
            else -> {
                throw Exception("Unknown StringType")
            }
        }
        typeToSort = type
        sortAndSearchInList()
    }

    companion object {
        const val MIN_CHAR_TO_SEARCH_TEXT = 3
    }
}

