package com.libbib.spewnikpl.di

import android.app.Application
import com.libbib.spewnikpl.data.SongListDao
import com.libbib.spewnikpl.data.SongListRepositoryImpl
import com.libbib.spewnikpl.data.SongRoomDatabase
import com.libbib.spewnikpl.data.firebase.FirebaseRepository
import com.libbib.spewnikpl.data.options.OptionsRepositoryImpl
import com.libbib.spewnikpl.domain.SongListRepository
import com.libbib.spewnikpl.domain.options.OptionsRepository
import com.libbib.spewnikpl.domain.remoteDB.RemoteDatabaseRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun bindSongListRepository(impl: SongListRepositoryImpl): SongListRepository {
        return impl
    }

    @Provides
    fun bindOptionsRepository(impl: OptionsRepositoryImpl): OptionsRepository {
        return impl
    }

    @Provides
    fun provideSongListDao(application: Application): SongListDao {
        return SongRoomDatabase.getDatabase(application).songListDao()
    }

    @Provides
    fun bindDatabaseRepository(impl: FirebaseRepository): RemoteDatabaseRepository {
        return impl
    }

}