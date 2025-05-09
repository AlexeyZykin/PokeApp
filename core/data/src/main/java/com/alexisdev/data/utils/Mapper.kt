package com.alexisdev.data.utils

import com.alexisdev.domain.model.PokeImage
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.model.PokemonDetails
import com.alexisdev.domain.model.PokemonStat
import com.alexisdev.domain.model.PokemonType
import com.alexisdev.domain.model.Stat
import com.alexisdev.domain.model.StatType
import com.alexisdev.poke_api.dto.PokemonDetailsDto
import com.alexisdev.poke_api.dto.PokemonStatDto
import com.alexisdev.poke_api.dto.PokemonTypeDto
import com.alexisdev.poke_api.dto.StatDto

fun PokemonDetailsDto.toPokemon() = Pokemon(
    id = id,
    name = name,
    height = height,
    weight = weight,
    stats = stats.map { it.toPokemonStat() },
    image = PokeImage(this.sprites.frontDefault)
)

fun PokemonDetailsDto.toPokemonDetails() = PokemonDetails(
    id = id,
    name = name,
    height = height,
    weight = weight,
    types = types.map { it.type.toPokemonType() },
    stats = stats.map { it.toPokemonStat() },
    image = PokeImage(sprites.frontDefault)
)

fun PokemonTypeDto.toPokemonType() = PokemonType(name)

fun PokemonStatDto.toPokemonStat() = PokemonStat(baseStat, stat = stat.toStat())

fun StatDto.toStat() = Stat(name)