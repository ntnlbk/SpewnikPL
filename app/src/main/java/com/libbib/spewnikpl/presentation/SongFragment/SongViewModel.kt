package com.libbib.spewnikpl.presentation.SongFragment


import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.libbib.spewnikpl.domain.GetSongUseCase
import com.libbib.spewnikpl.domain.Song
import com.libbib.spewnikpl.domain.TransposeSongUseCase
import com.libbib.spewnikpl.domain.options.GetOptionsUseCase
import com.libbib.spewnikpl.domain.options.Options
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SongViewModel @AssistedInject constructor(
    @Assisted private val songId: Int,
    private val getSongUseCase: GetSongUseCase,
    private val getOptionsUseCase: GetOptionsUseCase,
    private val transposeSongUseCase: TransposeSongUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<SongFragmentState>(SongFragmentState.Progress)
    val state = _state.asStateFlow()

    private lateinit var options: Options
    private lateinit var song: Song

    fun updateScreen() {
        viewModelScope.launch {
            song = getSongUseCase.invoke(songId).first()
            options = getOptionsUseCase()
            parseSong(song)
        }
    }

    private fun parseSong(song: Song) {
        _state.value = SongFragmentState.Progress
        val songName = song.name
        val songText = song.text

        val spannableSongText = SpannableString(parseSongText(songText))
        _state.value = SongFragmentState.Content(songName, spannableSongText, options.textSize)


    }

    private fun parseSongText(songText: String): SpannableStringBuilder {
        val lines = songText.lines()
        val resultSpannable = SpannableStringBuilder()
        for (line in lines) {
            var spannableLine: SpannableString
            if (isChordLine(line)) {
                if(options.isChordsVisible){
                    var chordLine = formatChordLine(line)
                    if (options.transposeInt != ZERO_TRANSPOSE)
                        chordLine = transposeSongUseCase(chordLine, options.transposeInt)
                    spannableLine = SpannableString(chordLine)
                    spannableLine.setSpan(
                        ForegroundColorSpan(options.chordsColor),
                        0,
                        chordLine.length - 1,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                } else{
                    spannableLine = SpannableString(EMPTY_STRING)
                }

            } else {
                spannableLine = SpannableString(line + NEW_LINE_CHAR)
            }

            resultSpannable.append(spannableLine)
        }

        return resultSpannable

    }

    private fun formatChordLine(line: String) = line.filterNot { it == CHORD_LINE_BEGIN } + NEW_LINE_CHAR

    private fun isChordLine(line: String) = line.isNotEmpty() && line[0] == CHORD_LINE_BEGIN

    companion object {

        private const val ZERO_TRANSPOSE = 0
        private const val EMPTY_STRING = ""
        const val CHORD_LINE_BEGIN = '|'
        private const val NEW_LINE_CHAR = '\n'

        @Suppress("UNCHECKED_CAST")
        fun factory(
            factory: Factory,
            songId: Int,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    factory.create(songId) as T
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(songId: Int): SongViewModel
    }
}