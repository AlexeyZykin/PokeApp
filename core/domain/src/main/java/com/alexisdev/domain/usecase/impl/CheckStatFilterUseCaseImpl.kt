package com.alexisdev.domain.usecase.impl

import com.alexisdev.domain.model.StatType
import com.alexisdev.domain.repo.PokeRepo
import com.alexisdev.domain.usecase.api.CheckStatFilterUseCase

internal class CheckStatFilterUseCaseImpl(private val pokeRepo: PokeRepo) :
    CheckStatFilterUseCase {

    override suspend fun execute(filter: StatType) {
        pokeRepo.saveCheckedStatFilter(filter)
    }
}