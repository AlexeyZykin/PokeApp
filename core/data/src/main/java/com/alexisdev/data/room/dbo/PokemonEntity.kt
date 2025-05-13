package com.alexisdev.data.room.dbo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexisdev.data.room.db.PokeDatabase


@Entity(tableName = PokeDatabase.POKEMON_TABLE_NAME)
data class PokemonEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    @ColumnInfo(name = "stats")
    val stats: List<PokemonStatEntity>,
    @Embedded
    val image: PokeImageEntity,
    val isTop: Boolean
)

