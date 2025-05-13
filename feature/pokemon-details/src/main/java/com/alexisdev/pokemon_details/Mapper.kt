package com.alexisdev.pokemon_details

import com.alexisdev.domain.model.PokemonStat
import com.alexisdev.domain.model.StatType

fun PokemonStat.toStatType(statStr: String) = when (statStr.lowercase()) {
    "attack" -> StatType.ATTACK
    "defense" -> StatType.DEFENSE
    "hp" -> StatType.HP
    else -> StatType.UNDEFINED
}