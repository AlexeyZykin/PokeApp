package com.alexisdev.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexisdev.common.Response
import com.alexisdev.data.datasource.PokePagingDataSource
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.model.PokemonDetails
import com.alexisdev.domain.repo.PokeRepo
import com.alexisdev.poke_api.PokeApi
import kotlinx.coroutines.flow.Flow

internal class PokeRepoImpl(private val pokeApi: PokeApi) : PokeRepo {
    override fun getPokemons(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = {
                PokePagingDataSource(pokeApi)
            }
        ).flow
    }

    override fun getPokemonDetails(name: String): Flow<Response<PokemonDetails>> {
        TODO("Not yet implemented")
    }
}