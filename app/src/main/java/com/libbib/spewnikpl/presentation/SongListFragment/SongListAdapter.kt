package com.libbib.spewnikpl.presentation.SongListFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.libbib.spewnikpl.databinding.SongItemBinding
import com.libbib.spewnikpl.domain.Song
import javax.inject.Inject
import kotlin.random.Random

class SongListAdapter @Inject constructor() : ListAdapter<Song, SongListAdapter.SongViewHolder>(
    SongDiffCallback()
) {

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
    }
    fun launchRandomSong(){
        onSongItemClickListener?.let { it(getItem(Random.nextInt(0, currentList.size))) }
    }
}