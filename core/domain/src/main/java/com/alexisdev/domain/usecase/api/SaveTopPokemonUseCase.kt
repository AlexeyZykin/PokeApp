package com.alexisdev.domain.usecase.api

import com.alexisdev.domain.model.Pokemon

interface SaveTopPokemonUseCase {
    suspend fun execute(pokemon: Pokemon?)
}