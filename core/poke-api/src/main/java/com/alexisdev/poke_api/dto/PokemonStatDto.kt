package com.alexisdev.poke_api.dto

import com.google.gson.annotations.SerializedName

data class PokemonStatDto(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatDto
)


