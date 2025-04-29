package com.alexisdev.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val stats: List<PokemonStat>
)
