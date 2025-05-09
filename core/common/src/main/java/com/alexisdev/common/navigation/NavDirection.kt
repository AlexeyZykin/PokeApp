package com.alexisdev.common.navigation

sealed interface NavDirection {

    data class PokemonCatalogToPokemonDetails(val pokeName: String) : NavDirection

}