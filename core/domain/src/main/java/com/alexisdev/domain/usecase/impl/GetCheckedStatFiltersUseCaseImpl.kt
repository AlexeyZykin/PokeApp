package com.alexisdev.domain.usecase.impl

import com.alexisdev.domain.model.StatType
import com.alexisdev.domain.repo.PokeRepo
import com.alexisdev.domain.usecase.api.GetCheckedStatFiltersUseCase
import kotlinx.coroutines.flow.Flow

internal class GetCheckedStatFiltersUseCaseImpl(private val pokeRepo: PokeRepo) :
    GetCheckedStatFiltersUseCase {

    override fun execute(): Flow<Set<StatType>> {
        return pokeRepo.getFilterFlow()
    }
}