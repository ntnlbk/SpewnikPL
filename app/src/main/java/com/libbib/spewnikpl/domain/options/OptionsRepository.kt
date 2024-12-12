package com.libbib.spewnikpl.domain.options

interface OptionsRepository {
    suspend fun getOptions(): Options

    suspend fun saveOptions(options: Options)
}