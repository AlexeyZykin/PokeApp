package com.alexisdev.poke_api.dto

data class PokemonDto(
    val name: String,
    val url: String
) {
    fun getPokemonId(): Int? {
        return url.split("/")
            .last { it.isNotEmpty() }
            .toIntOrNull()
    }
}
