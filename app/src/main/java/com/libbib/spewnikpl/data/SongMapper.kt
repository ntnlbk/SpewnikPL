package com.libbib.spewnikpl.data

import android.app.Application
import com.libbib.spewnikpl.domain.Song
import com.libbib.spewnikpl.domain.SongType
import com.libbib.spewnikpl.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SongMapper @Inject constructor(
    private val context: Application,
) {

    fun mapDbModelToEntity(dbModel: SongDbModel): Song {

        return Song(
            dbModel.name,
            dbModel.text,
            dbModel.author,
            mapStringTypesToSetTypes(dbModel.types),
            dbModel.types,
            dbModel.id
        )
    }

    fun mapListDbModelToListEntity(list: Flow<List<SongDbModel>>): Flow<List<Song>> {
        return list.map { it.map { dbModel -> mapDbModelToEntity(dbModel) } }
    }

    private fun mapStringTypesToSetTypes(stringTypes: String): Set<SongType> {
        val shortStringType = context.getString(R.string.short_songs).lowercase()
        val longStringType = context.getString(R.string.long_songs).lowercase()
        val partOfMassStringType = context.getString(R.string.parts).lowercase()
        val carolStringType = context.getString(R.string.carols).lowercase()

        val setTypes = mutableSetOf<SongType>()
        val stringTypesLowerCase = stringTypes.lowercase()
        setTypes.add(SongType.ALL)
        if (stringTypesLowerCase.contains(shortStringType))
            setTypes.add(SongType.SHORT)
        if (stringTypesLowerCase.contains(longStringType))
            setTypes.add(SongType.LONG)
        if (stringTypesLowerCase.contains(partOfMassStringType))
            setTypes.add(SongType.PART_OF_MASS)
        if (stringTypesLowerCase.contains(carolStringType))
            setTypes.add(SongType.CAROLS)
        return setTypes
    }


}
