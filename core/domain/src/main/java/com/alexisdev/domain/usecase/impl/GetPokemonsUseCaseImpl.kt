package com.alexisdev.domain.usecase.impl

import androidx.paging.PagingData
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.repo.PokeRepo
import com.alexisdev.domain.usecase.api.GetPokemonsUseCase
import kotlinx.coroutines.flow.Flow

internal class GetPokemonsUseCaseImpl(private val pokeRepo: PokeRepo) : GetPokemonsUseCase {
    override fun execute(): Flow<PagingData<Pokemon>> {
        return pokeRepo.getPokemons()
    }

}