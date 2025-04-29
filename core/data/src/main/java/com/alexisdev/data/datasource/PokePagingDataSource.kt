package com.alexisdev.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexisdev.data.utils.toPokemon
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.poke_api.PokeApi

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

            val pokemonList = response.results.map { it.toPokemon() }

            val nextPageNumber = if (pokemonList.isEmpty()) null else offset + pageSize
            val prevPageNumber = if (offset == PokeApi.START_OFFSET_VALUE) null else offset - pageSize
            return LoadResult.Page(pokemonList, prevPageNumber, nextPageNumber)

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}