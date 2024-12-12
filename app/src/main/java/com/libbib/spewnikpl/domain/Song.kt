package com.libbib.spewnikpl.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val name: String,
    val text: String,
    val author: String,
    val types: Set<SongType>,
    val typesString: String,
    var id: Int = UNDEFINED_ID
) : Parcelable {
    companion object {
        private const val UNDEFINED_ID = 0
    }
}