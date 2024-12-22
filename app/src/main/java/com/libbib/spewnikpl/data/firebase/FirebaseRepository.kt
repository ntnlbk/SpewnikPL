package com.libbib.spewnikpl.data.firebase


import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.libbib.spewnikpl.domain.remoteDB.RemoteDatabaseRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FirebaseRepository @Inject constructor() : RemoteDatabaseRepository {

    private var resultFlow = MutableSharedFlow<Int>()


    override fun getActualVersionUseCase(): SharedFlow<Int> {
        val database = Firebase.database("https://spewnikpl-default-rtdb.europe-west1.firebasedatabase.app/").reference

        val versionRef = database.child(FIREBASE_REALTIME_DATABASE_VERSION_NAME)

        versionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = Integer.valueOf(snapshot.value.toString())
                runBlocking {
                    resultFlow.emit(result)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return resultFlow.asSharedFlow()
    }
    companion object{
        private const val FIREBASE_REALTIME_DATABASE_VERSION_NAME = "update"
    }
}