package com.alexisdev.domain.usecase.api

import com.alexisdev.domain.model.StatType
import kotlinx.coroutines.flow.Flow

interface GetCheckedStatFiltersUseCase {
    fun execute(): Flow<Set<StatType>>
}