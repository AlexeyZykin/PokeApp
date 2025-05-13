package com.alexisdev.domain.di

import com.alexisdev.domain.usecase.api.CheckStatFilterUseCase
import com.alexisdev.domain.usecase.api.FindPokemonByFiltersUseCase
import com.alexisdev.domain.usecase.api.GetCheckedStatFiltersUseCase
import com.alexisdev.domain.usecase.api.GetPokemonDetailsUseCase
import com.alexisdev.domain.usecase.api.GetPokemonsUseCase
import com.alexisdev.domain.usecase.api.SaveTopPokemonUseCase
import com.alexisdev.domain.usecase.impl.CheckStatFilterUseCaseImpl
import com.alexisdev.domain.usecase.impl.FindPokemonByFiltersUseCaseImpl
import com.alexisdev.domain.usecase.impl.GetCheckedStatFiltersUseCaseImpl
import com.alexisdev.domain.usecase.impl.GetPokemonDetailsUseCaseImpl
import com.alexisdev.domain.usecase.impl.GetPokemonsUseCaseImpl
import com.alexisdev.domain.usecase.impl.SaveTopPokemonUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    factory<GetPokemonsUseCase> { GetPokemonsUseCaseImpl(get()) }
    factory<GetPokemonDetailsUseCase> { GetPokemonDetailsUseCaseImpl(get()) }
    factory<FindPokemonByFiltersUseCase> { FindPokemonByFiltersUseCaseImpl(get()) }
    factory<CheckStatFilterUseCase> { CheckStatFilterUseCaseImpl(get()) }
    factory<GetCheckedStatFiltersUseCase> { GetCheckedStatFiltersUseCaseImpl(get()) }
    factory<SaveTopPokemonUseCase> { SaveTopPokemonUseCaseImpl(get()) }
}