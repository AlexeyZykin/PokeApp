package com.alexisdev.domain.usecase.api

import com.alexisdev.common.Response
import com.alexisdev.domain.model.PokemonDetails
import kotlinx.coroutines.flow.Flow

interface GetPokemonDetailsUseCase {
    fun execute(name: String): Flow<Response<PokemonDetails>>
}