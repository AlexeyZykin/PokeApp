package com.alexisdev.poke_api.dto


data class PokemonDetailsDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val stats: List<PokemonStatDto>,
    val sprites: PokeSpritesDto
)

