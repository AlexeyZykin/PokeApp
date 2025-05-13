package com.alexisdev.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexisdev.data.room.db.PokeDatabase
import com.alexisdev.data.room.dbo.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM ${PokeDatabase.REMOTE_KEYS_TABLE_NAME} WHERE pokeId = :pokeId")
    suspend fun remoteKeysRepoId(pokeId: Int): RemoteKeys?

    @Query("DELETE FROM ${PokeDatabase.REMOTE_KEYS_TABLE_NAME}")
    suspend fun clearRemoteKeys()

}