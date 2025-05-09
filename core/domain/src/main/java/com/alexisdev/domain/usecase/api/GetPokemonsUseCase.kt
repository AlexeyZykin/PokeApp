package com.alexisdev.domain.usecase.api

import androidx.paging.PagingData
import com.alexisdev.common.Response
import com.alexisdev.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface GetPokemonsUseCase {
    fun execute(isRandomStartPositionMode: Boolean): Flow<PagingData<Pokemon>>
}