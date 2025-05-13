package com.alexisdev.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.alexisdev.common.Response
import com.alexisdev.data.datasource.PokeRemoteMediator
import com.alexisdev.data.room.db.PokeDatabase
import com.alexisdev.data.utils.toPokemon
import com.alexisdev.data.utils.toPokemonDetails
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.model.PokemonDetails
import com.alexisdev.domain.model.StatType
import com.alexisdev.domain.repo.PokeRepo
import com.alexisdev.poke_api.PokeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class PokeRepoImpl(
    private val pokeApi: PokeApi,
    private val db: PokeDatabase
    ) : PokeRepo {

    private val _checkedFiltersFlow = MutableStateFlow<Set<StatType>>(emptySet())
    private val checkedFiltersFlow: StateFlow<Set<StatType>> get() = _checkedFiltersFlow

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemons(isRandomStartPositionMode: Boolean): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = PokeApi.MAX_PAGE_SIZE,
                initialLoadSize = PokeApi.MAX_PAGE_SIZE,
                prefetchDistance = 10
            ),
            remoteMediator = PokeRemoteMediator(pokeApi, db, isRandomStartPositionMode),
            pagingSourceFactory = { db.getPokeDao().pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toPokemon() }
        }
    }

    override fun getPokemonDetails(name: String): Flow<Response<PokemonDetails>> {
        return flow {
            try {
                val pokemon = pokeApi.fetchPokemonDetails(name).toPokemonDetails()
                emit(Response.Success(data = pokemon))
            } catch (e: Exception) {
                emit(Response.Error(message = e.message ?: "Error"))
            }
        }
    }

    override suspend fun getLoadedPokemons(): List<Pokemon> {
        return db.getPokeDao().getAll().map { it.toPokemon() }
    }

    override suspend fun saveCheckedStatFilter(filter: StatType) {
        _checkedFiltersFlow.update { checkedFilters ->
            if (checkedFilters.contains(filter)) {
                checkedFilters - filter
            } else {
                checkedFilters + filter
            }
        }
    }

    override fun getFilterFlow(): Flow<Set<StatType>> {
        return checkedFiltersFlow
    }

    override suspend fun saveTopPokemon(pokemon: Pokemon?) {
        db.withTransaction {
            db.getPokeDao().updateTopPokemon(pokemon?.id ?: -1)
        }
    }
}