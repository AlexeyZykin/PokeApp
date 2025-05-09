package com.alexisdev.poke_api

import com.alexisdev.poke_api.dto.PokemonDetailsDto
import com.alexisdev.poke_api.dto.PokeApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ) : PokeApiResponse

    @GET("pokemon")
    suspend fun fetchPokeApiInfo() : PokeApiResponse

    @GET("pokemon/{name}/")
    suspend fun fetchPokemonDetails(
        @Path("name") name: String
    ) : PokemonDetailsDto

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
        const val MAX_PAGE_SIZE = 30
        const val START_OFFSET_VALUE = 0
    }
}