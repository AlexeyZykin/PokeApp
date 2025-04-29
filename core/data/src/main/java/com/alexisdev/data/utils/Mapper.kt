package com.alexisdev.data.utils

import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.model.PokemonStat
import com.alexisdev.domain.model.Stat
import com.alexisdev.poke_api.dto.PokemonDto
import com.alexisdev.poke_api.dto.PokemonStatDto
import com.alexisdev.poke_api.dto.StatDto

fun PokemonDto.toPokemon() = Pokemon(id = id, name = name, stats = stats.map { it.toPokemonStat() })

fun PokemonStatDto.toPokemonStat() = PokemonStat(baseStat = baseStat, stat = stat.toStat())

fun StatDto.toStat() = Stat(name)

