package com.alexisdev.domain.repo

import androidx.paging.PagingData
import com.alexisdev.common.Response
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.model.PokemonDetails
import com.alexisdev.domain.model.StatType
import kotlinx.coroutines.flow.Flow

interface PokeRepo {
    fun getPokemons(isRandomStartPositionMode: Boolean): Flow<PagingData<Pokemon>>
    fun getPokemonDetails(name: String): Flow<Response<PokemonDetails>>
    suspend fun getLoadedPokemons(): List<Pokemon>
    suspend fun saveCheckedStatFilter(filter: StatType)
    fun getFilterFlow(): Flow<Set<StatType>>
    suspend fun saveTopPokemon(pokemon: Pokemon?)
}