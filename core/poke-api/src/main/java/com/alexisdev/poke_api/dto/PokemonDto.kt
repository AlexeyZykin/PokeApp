package com.alexisdev.poke_api.dto

data class PokemonDto(
    val id: Int,
    val name: String,
    val stats: List<PokemonStatDto>
)