package com.alexisdev.domain.di

import com.alexisdev.domain.usecase.api.GetPokemonsUseCase
import com.alexisdev.domain.usecase.impl.GetPokemonsUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    factory<GetPokemonsUseCase> { GetPokemonsUseCaseImpl(get()) }
}