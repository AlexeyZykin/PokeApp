package com.alexisdev.poke_api.dto

data class PokeApiResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<PokemonDto>
)