package com.alexisdev.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val stats: List<PokemonStat>,
    val image: PokeImage
)
