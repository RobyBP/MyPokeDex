package com.example.mypokedex.database

import com.example.mypokedex.data.remote.PokeApi
import com.example.mypokedex.data.remote.response.PokemonList
import com.example.mypokedex.util.NetworkViewState
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val pokeApi: PokeApi
){
    suspend fun getPokemonList(limit: Int, offset: Int): NetworkViewState<PokemonList> {
        val response = try {
            pokeApi.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return NetworkViewState.Error(null, e.message)
        }
        return NetworkViewState.Success(response)
    }
}