package com.alexisdev.domain.repo

import androidx.paging.PagingData
import com.alexisdev.common.Response
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.model.PokemonDetails
import kotlinx.coroutines.flow.Flow

interface PokeRepo {
    fun getPokemons(): Flow<PagingData<Pokemon>>
    fun getPokemonDetails(name: String): Flow<Response<PokemonDetails>>
}