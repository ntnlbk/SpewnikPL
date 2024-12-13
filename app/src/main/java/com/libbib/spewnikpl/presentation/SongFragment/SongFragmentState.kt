package com.libbib.spewnikpl.presentation.SongFragment


import android.text.SpannableString

sealed class SongFragmentState {
    data object Progress : SongFragmentState()
    class Content(
        val name: String,
        val text: SpannableString,
        val textSizeFromOptions: Int = 0
    ) : SongFragmentState()
}