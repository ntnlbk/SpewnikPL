package com.libbib.spewnikpl.domain.options
data class Options(
    var isChordsVisible: Boolean,
    var transposeInt: Int,
    var chordsColor: Int,
    var textSize: Int,
    var isDarkMode: Boolean,
) {
    companion object {
        const val CHORDS_VISIBLE_KEY = "chords_visible_key"
        const val TRANSPOSE_KEY = "transpose_key"
        const val CHORDS_COLOR_KEY = "chords_color_key"
        const val TEXT_SIZE_KEY = "text_size_key"
        const val DARK_MODE_KEY = "dark_mode_key"
    }
}