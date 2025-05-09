package com.alexisdev.data.di

import com.alexisdev.data.datasource.PokePagingDataSource
import com.alexisdev.data.repo.PokeRepoImpl
import com.alexisdev.domain.repo.PokeRepo
import org.koin.dsl.module

val dataModule = module {
    single<PokeRepo> { PokeRepoImpl(get()) }
}