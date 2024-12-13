package com.libbib.spewnikpl.di

import android.app.Application
import android.app.UiModeManager
import android.app.UiModeManager.MODE_NIGHT_NO
import android.app.UiModeManager.MODE_NIGHT_YES
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.libbib.spewnikpl.data.options.OptionsRepositoryImpl
import com.libbib.spewnikpl.domain.options.GetOptionsUseCase
import kotlinx.coroutines.runBlocking

class SpewnikApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
    val getOptionsUseCase = GetOptionsUseCase(OptionsRepositoryImpl(this))
    override fun onCreate() {
        super.onCreate()
        darkMode()
    }

    fun darkMode() {
        runBlocking {
            val options = getOptionsUseCase()
            if (options.isDarkMode) {
                goNightMode()
            } else {
                goNoNightMode()
            }
        }
    }

    fun goNoNightMode() {
        val uiManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiManager.setApplicationNightMode(MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun goNightMode() {
        val uiManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiManager.setApplicationNightMode(MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}