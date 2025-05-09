package com.alexisdev.domain.model

data class PokemonDetails(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonType>,
    val stats: List<PokemonStat>,
    val image: PokeImage
)
