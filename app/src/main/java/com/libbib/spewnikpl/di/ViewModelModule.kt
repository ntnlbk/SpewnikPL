package com.libbib.spewnikpl.di

import androidx.lifecycle.ViewModel
import com.libbib.spewnikpl.presentation.OptionsFragment.OptionsViewModel
import com.libbib.spewnikpl.presentation.SongListFragment.SongListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(SongListViewModel::class)
    @Binds
    fun bindSongListViewModel(impl: SongListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(OptionsViewModel::class)
    @Binds
    fun bindOptionsViewModel(impl: OptionsViewModel): ViewModel

}