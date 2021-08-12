package com.example.mypokedex.screen.pokemonlist

import com.example.mypokedex.core.BaseViewModel
import com.example.mypokedex.data.PokedexListEntry
import com.example.mypokedex.database.PokemonRepository
import com.example.mypokedex.util.Constants.PAGE_SIZE
import com.example.mypokedex.util.NetworkViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : BaseViewModel() {

    private var currentPage = 0
    var pokemonList = MutableStateFlow<List<PokedexListEntry>>(listOf())
    var error = MutableStateFlow("")
    var isLoading = MutableStateFlow(false)
    var endReached = MutableStateFlow(false)

    init {
        loadPokemonPaginated()
    }

    private fun loadPokemonPaginated() {
        isLoading.value = true
        runCommand {
            when(val result = repository.getPokemonList(PAGE_SIZE, currentPage + PAGE_SIZE)) {
                is NetworkViewState.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if(entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(entry.name, url, index)
                    }
                    currentPage++
                    error.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries
                }
                is NetworkViewState.Error -> {
                    error.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }
}
