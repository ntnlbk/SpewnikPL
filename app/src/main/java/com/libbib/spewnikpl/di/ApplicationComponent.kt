package com.libbib.spewnikpl.di

import android.app.Application
import com.libbib.spewnikpl.presentation.MainActivity
import com.libbib.spewnikpl.presentation.OptionsFragment.OptionsFragment
import com.libbib.spewnikpl.presentation.SongFragment.SongFragment
import com.libbib.spewnikpl.presentation.SongListFragment.SongListFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(songListFragment: SongListFragment)

    fun inject(songFragment: SongFragment)

    fun inject(optionsFragment: OptionsFragment)

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface ApplicationComponentFactory{
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}