package com.libbib.spewnikpl.presentation.SongListFragment

import android.app.Application
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.libbib.spewnikpl.R
import com.libbib.spewnikpl.databinding.SongItemBinding
import com.libbib.spewnikpl.domain.Song
import com.libbib.spewnikpl.presentation.SongFragment.SongViewModel
import javax.inject.Inject
import kotlin.random.Random

class SongListAdapter @Inject constructor(
    private val application: Application,
) : ListAdapter<Song, SongListAdapter.SongViewHolder>(
    SongDiffCallback()
) {

    private var isSearch: Boolean = false
    private var stringToColor: String = EMPTY_STRING

    var onSongItemClickListener: ((Song) -> Unit)? = null

    class SongViewHolder(val binding: SongItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        holder.binding.tvName.text = song.name
        holder.binding.tvTypes.text = song.typesString
        holder.binding.root.setOnClickListener {
            onSongItemClickListener?.invoke(song)
        }
        if (isSearch) {
            val songName = song.name
            val idStart = songName.lowercase().indexOf(stringToColor)
            if (idStart >= 0) {
                val span = SpannableStringBuilder(songName)
                span.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(application, R.color.yellow)),
                    idStart,
                    idStart + stringToColor.length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                holder.binding.tvName.text = span
            }
            if (stringToColor.length > SongListViewModel.MIN_CHAR_TO_SEARCH_TEXT) {
                var songText = song.text
                val lines = songText.lines()
                songText = lines
                    .filterNot {
                        it.isEmpty()
                    }
                    .filterNot {
                        it[0]== SongViewModel.CHORD_LINE_BEGIN
                    }
                    .joinToString()
                val idStartText = songText.lowercase().indexOf(stringToColor)
                if (idStartText >= 0) {

                    var subString =
                        if (idStartText + HOW_MANY_CHARS_TO_COLOR_IN_TEXT < songText.length) songText.substring(
                            idStartText,
                            idStartText + HOW_MANY_CHARS_TO_COLOR_IN_TEXT
                        ) else songText.substring(idStartText, songText.length)
                    subString = subString.filter { it != '\n' }
                    val span = SpannableStringBuilder(subString)
                    span.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(application, R.color.yellow)),
                        0,
                        stringToColor.length,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                    holder.binding.tvTypes.text = span
                }
            }

        }
    }

    fun launchRandomSong() {
        onSongItemClickListener?.let { it(getItem(Random.nextInt(0, currentList.size))) }
    }

    fun colorResults(stringToColor: String) {
        isSearch = true
        this.stringToColor = stringToColor
    }

    fun resetColors() {
        isSearch = false
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val HOW_MANY_CHARS_TO_COLOR_IN_TEXT = 50
    }
}