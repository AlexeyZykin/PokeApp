package com.alexisdev.domain.usecase.api

import com.alexisdev.domain.model.StatType

interface CheckStatFilterUseCase {
    suspend fun execute(filter: StatType)
}