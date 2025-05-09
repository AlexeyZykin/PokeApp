package com.alexisdev.domain.usecase.impl

import com.alexisdev.common.Response
import com.alexisdev.domain.model.PokemonDetails
import com.alexisdev.domain.repo.PokeRepo
import com.alexisdev.domain.usecase.api.GetPokemonDetailsUseCase
import kotlinx.coroutines.flow.Flow

internal class GetPokemonDetailsUseCaseImpl(private val pokeRepo: PokeRepo) : GetPokemonDetailsUseCase {
    override fun execute(name: String): Flow<Response<PokemonDetails>> {
        return pokeRepo.getPokemonDetails(name)
    }
}