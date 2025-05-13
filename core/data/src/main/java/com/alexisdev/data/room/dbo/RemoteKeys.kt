package com.alexisdev.data.room.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexisdev.data.room.db.PokeDatabase

@Entity(tableName = PokeDatabase.REMOTE_KEYS_TABLE_NAME)
data class RemoteKeys(
    @PrimaryKey
    val pokeId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)