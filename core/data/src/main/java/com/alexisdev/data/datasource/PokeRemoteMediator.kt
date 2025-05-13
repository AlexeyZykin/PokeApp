package com.alexisdev.data.datasource

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.alexisdev.data.room.db.PokeDatabase
import com.alexisdev.data.room.dbo.PokemonEntity
import com.alexisdev.data.room.dbo.RemoteKeys
import com.alexisdev.data.utils.PokeUtils
import com.alexisdev.data.utils.toPokemon
import com.alexisdev.data.utils.toPokemonEntity
import com.alexisdev.poke_api.PokeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList

@OptIn(ExperimentalPagingApi::class)
class PokeRemoteMediator(
    private val pokeApi: PokeApi,
    private val db: PokeDatabase,
    private val isRandomStartPositionMode: Boolean
) : RemoteMediator<Int, PokemonEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d("PokeTestRM", "REFRESH")
                    val pokeApiInfo = pokeApi.fetchPokeApiInfo()
                    if (isRandomStartPositionMode)
                        PokeUtils.generateRandomOffsetValue(
                            pageSize = PokeApi.MAX_PAGE_SIZE,
                            upperBound = pokeApiInfo.count
                        )
                    else
                        getRemoteKeyClosestToCurrentPosition(state)?.nextKey?.minus(PokeApi.MAX_PAGE_SIZE)
                            ?: PokeApi.START_OFFSET_VALUE
                    }
                LoadType.PREPEND -> {
                    Log.d("PokeTestRM", "PREPEND")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    Log.d("PokeTestRM", "APPEND")
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val response = pokeApi.fetchPokemonList(limit = PokeApi.MAX_PAGE_SIZE, offset = offset)
            val totalCount = response.count
            val pokemons = response.results.asFlow()
                .buffer()
                .flatMapMerge(concurrency = 5) { pokemonDto ->
                    flow {
                        emit(pokeApi.fetchPokemonDetails(pokemonDto.name).toPokemonEntity())
                    }.flowOn(Dispatchers.IO)
                }
                .catch { Log.d("PokeTestError", it.message.toString()) }
                .toList()
                .sortedBy { it.id }

            Log.d("PokeTest", pokemons.size.toString())

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getPokeDao().clear()
                    db.remoteKeysDao().clearRemoteKeys()
                }
                val prevKey = if (offset == PokeApi.START_OFFSET_VALUE) null else offset - PokeApi.MAX_PAGE_SIZE
                val nextKey = if (offset + PokeApi.MAX_PAGE_SIZE < totalCount) {
                    offset + PokeApi.MAX_PAGE_SIZE
                } else {
                    null
                }
                val keys = pokemons.map {
                    RemoteKeys(pokeId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys)
                db.getPokeDao().insertAll(pokemons)
            }

            val endOfPaginationReached = pokemons.isEmpty()
            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: Exception) {
            Log.d("PokeTest. Paging source Error:", e.message.toString())

            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { poke ->
                db.remoteKeysDao().remoteKeysRepoId(poke.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PokemonEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}