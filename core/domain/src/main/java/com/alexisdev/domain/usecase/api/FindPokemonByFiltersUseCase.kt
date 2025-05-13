package com.alexisdev.domain.usecase.api

import com.alexisdev.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface FindPokemonByFiltersUseCase {
    fun execute(): Flow<Pokemon?>
}