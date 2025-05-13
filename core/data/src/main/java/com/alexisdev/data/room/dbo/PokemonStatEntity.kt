package com.alexisdev.data.room.dbo

import kotlinx.serialization.Serializable

@Serializable
data class PokemonStatEntity(
    val baseStat: Int,
    val stat: StatEntity
)
