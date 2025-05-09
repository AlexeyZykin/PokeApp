package com.alexisdev.pokemon_main.di

import com.alexisdev.pokemon_main.PokemonCatalogViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val pokemonMainFeatureModule = module {
    viewModelOf(::PokemonCatalogViewModel)
}