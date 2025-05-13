package com.alexisdev.data.utils

import androidx.room.TypeConverter
import com.alexisdev.data.room.dbo.PokemonStatEntity
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStats(stats: List<PokemonStatEntity>): String {
        return json.encodeToString(stats)
    }

    @TypeConverter
    fun toStats(jsonString: String): List<PokemonStatEntity> {
        return json.decodeFromString(jsonString)
    }
}