package com.libbib.spewnikpl.data.options

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.libbib.spewnikpl.di.ApplicationScope
import com.libbib.spewnikpl.domain.options.Options
import com.libbib.spewnikpl.domain.options.Options.Companion.CHORDS_COLOR_KEY
import com.libbib.spewnikpl.domain.options.Options.Companion.CHORDS_VISIBLE_KEY
import com.libbib.spewnikpl.domain.options.Options.Companion.DARK_MODE_KEY
import com.libbib.spewnikpl.domain.options.Options.Companion.TEXT_SIZE_KEY
import com.libbib.spewnikpl.domain.options.Options.Companion.TRANSPOSE_KEY
import com.libbib.spewnikpl.domain.options.OptionsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@ApplicationScope
class OptionsRepositoryImpl @Inject constructor(
    private val application: Application,
) : OptionsRepository {

    private val chordsVisibleKey = booleanPreferencesKey(CHORDS_VISIBLE_KEY)
    private val transposeKey = intPreferencesKey(TRANSPOSE_KEY)
    private val chordsColorKey = intPreferencesKey(CHORDS_COLOR_KEY)
    private val textSizeKey = intPreferencesKey(TEXT_SIZE_KEY)
    private val darkModeKey = booleanPreferencesKey(DARK_MODE_KEY)

    override suspend fun getOptions(): Options {
        val preferences = application.dataStore.data.first()
        val isChordsVisible = preferences[chordsVisibleKey] ?: true
        val transposeInt = preferences[transposeKey] ?: 0
        val chordsColor = preferences[chordsColorKey] ?: Color.Red.toArgb()
        val textSize = preferences[textSizeKey] ?: 0
        val isDarkMode = preferences[darkModeKey] ?: false
        Log.d("OptionsFragment from repo", chordsColor.toString())
        return Options(isChordsVisible, transposeInt, chordsColor, textSize, isDarkMode)
    }

    override suspend fun saveOptions(options: Options) {
        application.dataStore.edit { preferences ->
            preferences[chordsVisibleKey] = options.isChordsVisible
            preferences[transposeKey] = options.transposeInt
            preferences[chordsColorKey] = options.chordsColor
            preferences[textSizeKey] = options.textSize
            preferences[darkModeKey] = options.isDarkMode
            Log.d("OptionsFragment from repo to save", options.chordsColor.toString())
        }
    }
}