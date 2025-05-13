package com.alexisdev.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexisdev.data.room.db.PokeDatabase
import com.alexisdev.data.room.dbo.PokemonEntity

@Dao
interface PokeDao {

    @Query("SELECT * FROM ${PokeDatabase.POKEMON_TABLE_NAME} ORDER BY isTop DESC, id")
    fun pagingSource(): PagingSource<Int, PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Query("DELETE FROM ${PokeDatabase.POKEMON_TABLE_NAME}")
    suspend fun clear()

    @Query("SELECT * FROM ${PokeDatabase.POKEMON_TABLE_NAME} ORDER BY id")
    suspend fun getAll(): List<PokemonEntity>

    @Query("UPDATE ${PokeDatabase.POKEMON_TABLE_NAME} SET isTop = 0")
    suspend fun clearTopMarks()

    @Query("""
        UPDATE ${PokeDatabase.POKEMON_TABLE_NAME} 
        SET isTop = CASE 
            WHEN id = :pokemonId AND isTop = 0 THEN 1 
            WHEN id != :pokemonId AND isTop = 1 THEN 0 
            ELSE isTop 
        END
        WHERE id = :pokemonId OR isTop = 1
    """)
    suspend fun updateTopPokemon(pokemonId: Int)
}