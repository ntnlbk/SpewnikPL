package com.libbib.spewnikpl.domain.remoteDB

import kotlinx.coroutines.flow.SharedFlow

interface RemoteDatabaseRepository {
    fun getActualVersionUseCase(): SharedFlow<Int>
}