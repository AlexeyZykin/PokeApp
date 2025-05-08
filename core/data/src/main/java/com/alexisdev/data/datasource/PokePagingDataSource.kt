package com.alexisdev.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexisdev.data.utils.toPokemon
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.poke_api.PokeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList

class PokePagingDataSource(
    private val pokeApi: PokeApi
) : PagingSource<Int, Pokemon>() {
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        try {
            val offset = params.key ?: PokeApi.START_OFFSET_VALUE
            val pageSize = params.loadSize.coerceAtMost(PokeApi.MAX_PAGE_SIZE)

            val response = pokeApi.fetchPokemonList(limit = pageSize, offset = offset)

            val pokemonList = response.results
                .asFlow()
                .buffer()
                .flatMapMerge(concurrency = 5) { pokemonDto ->
                    flow {
                        emit(pokeApi.fetchPokemonDetails(pokemonDto.name).toPokemon())
                    }.flowOn(Dispatchers.IO)
                }
                .toList()

            Log.d("PokeTest. Paging source data response:", response.results.toString())

            Log.d("PokeTest. Paging source data pokemons:", pokemonList.toString())

            val nextPageNumber = if (pokemonList.isEmpty()) null else offset + pageSize
            val prevPageNumber = if (offset == PokeApi.START_OFFSET_VALUE) null else offset - pageSize
            return LoadResult.Page(pokemonList, prevPageNumber, nextPageNumber)

        } catch (e: Exception) {
            Log.d("PokeTest. Paging source Error:", e.message.toString())
            return LoadResult.Error(e)
        }
    }
}