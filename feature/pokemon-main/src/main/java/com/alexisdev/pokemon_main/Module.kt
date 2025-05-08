package com.alexisdev.pokemon_main

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val pokemonMainFeatureModule = module {
    viewModelOf(::PokemonCatalogViewModel)
}