package com.alexisdev.data.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alexisdev.data.room.dao.PokeDao
import com.alexisdev.data.room.dao.RemoteKeysDao
import com.alexisdev.data.room.dbo.PokemonEntity
import com.alexisdev.data.room.dbo.RemoteKeys
import com.alexisdev.data.utils.Converters

@Database(entities = [PokemonEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PokeDatabase : RoomDatabase() {

    abstract fun getPokeDao(): PokeDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DATABASE_NAME = "poke.db"
        const val POKEMON_TABLE_NAME = "pokemon"
        const val REMOTE_KEYS_TABLE_NAME = "remote_keys"
    }
}