package com.alexisdev.domain.usecase.impl

import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.repo.PokeRepo
import com.alexisdev.domain.usecase.api.SaveTopPokemonUseCase

internal class SaveTopPokemonUseCaseImpl(private val pokeRepo: PokeRepo) : SaveTopPokemonUseCase {
    override suspend fun execute(pokemon: Pokemon?) {
        pokeRepo.saveTopPokemon(pokemon)
    }
}