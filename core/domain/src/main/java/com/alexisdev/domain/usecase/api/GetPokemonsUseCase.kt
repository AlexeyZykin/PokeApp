package com.alexisdev.domain.usecase.api

import androidx.paging.PagingData
import com.alexisdev.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface GetPokemonsUseCase {
    fun execute(): Flow<PagingData<Pokemon>>
}