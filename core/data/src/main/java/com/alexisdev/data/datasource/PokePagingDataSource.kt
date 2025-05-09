package com.alexisdev.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexisdev.data.utils.PokeUtils
import com.alexisdev.data.utils.toPokemon
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.poke_api.PokeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.toList

class PokePagingDataSource(
    private val pokeApi: PokeApi,
    private val isRandomStartPositionMode: Boolean
) : PagingSource<Int, Pokemon>() {
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        try {
            val pokeApiInfo = pokeApi.fetchPokeApiInfo()
            val initialOffset = if (isRandomStartPositionMode)
                PokeUtils.generateRandomOffsetValue(
                    pageSize = PokeApi.MAX_PAGE_SIZE,
                    upperBound = pokeApiInfo.count
                )
            else PokeApi.START_OFFSET_VALUE
            val offset = params.key ?: initialOffset

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
                .catch { Log.d("PokeTestError", it.message.toString()) }
                .toList()

            Log.d("PokeTest. Paging source data pokemons:", pokemonList.size.toString())

            val nextPageNumber = if (pokemonList.isEmpty()) null else offset + pageSize
            val prevPageNumber = if (offset == initialOffset) null else offset - pageSize
            return LoadResult.Page(pokemonList, prevPageNumber, nextPageNumber)

        } catch (e: Exception) {
            Log.d("PokeTest. Paging source Error:", e.message.toString())
            return LoadResult.Error(e)
        }
    }
}