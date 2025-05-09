package com.alexisdev.pokemon_details

import com.alexisdev.domain.model.PokemonStat

enum class StatType {
    ATTACK, DEFENSE, HP, UNDEFINED
}

fun PokemonStat.toStatType(statStr: String) = when (statStr.lowercase()) {
    "attack" -> StatType.ATTACK
    "defense" -> StatType.DEFENSE
    "hp" -> StatType.HP
    else -> StatType.UNDEFINED
}