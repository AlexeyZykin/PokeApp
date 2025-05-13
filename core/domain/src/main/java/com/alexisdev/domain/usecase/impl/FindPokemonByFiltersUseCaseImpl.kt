package com.alexisdev.domain.usecase.impl

import android.util.Log
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.model.StatType
import com.alexisdev.domain.repo.PokeRepo
import com.alexisdev.domain.usecase.api.FindPokemonByFiltersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class FindPokemonByFiltersUseCaseImpl(
    private val pokeRepo: PokeRepo
) : FindPokemonByFiltersUseCase {

    override fun execute(): Flow<Pokemon?> {
        return pokeRepo.getFilterFlow()
            .flatMapLatest { filters ->
                flow {
                    val cachedPokemons = pokeRepo.getLoadedPokemons()
                    val result = when {
                        filters.isEmpty() -> {
                            null
                        }

                        cachedPokemons.isEmpty() -> {
                            null
                        }

                        else -> {
                            fun Pokemon.getStatValue(statType: StatType): Int {
                                return stats.firstOrNull {
                                    it.stat.name.equals(statType.name, ignoreCase = true)
                                }?.baseStat ?: 0
                            }

                            val pokemonWithStatTotalSum = hashMapOf<Pokemon, Int>()
                            cachedPokemons.forEach { pokemon ->
                                val totalSum = filters.sumOf { statType ->
                                    pokemon.getStatValue(statType)
                                }
                                pokemonWithStatTotalSum.put(pokemon, totalSum)
                            }

                            val topPokemon = pokemonWithStatTotalSum.maxByOrNull {
                                it.value
                            }?.key

                            topPokemon
                        }
                    }
                    emit(result)
                }
            }
    }
}