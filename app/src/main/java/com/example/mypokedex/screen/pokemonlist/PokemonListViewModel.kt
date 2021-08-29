package com.example.mypokedex.screen.pokemonlist

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.mypokedex.core.BaseViewModel
import com.example.mypokedex.data.PokedexListEntry
import com.example.mypokedex.database.PokemonRepository
import com.example.mypokedex.util.Constants.PAGE_SIZE
import com.example.mypokedex.util.NetworkViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val imageLoader: ImageLoader,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private var currentPage = 0
    var pokemonList = MutableStateFlow<List<PokedexListEntry>>(listOf())
    var error = MutableStateFlow("")
    var isLoading = MutableStateFlow(false)

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.getPokemonList(PAGE_SIZE, currentPage)) {
                is NetworkViewState.Success -> {
                    val pokedexEntries = result.data!!.results.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                        var dominantColor: Int

                        val request = ImageRequest.Builder(context)
                            .data(url)
                            .allowHardware(false) // Disable hardware bitmaps.
                            .build()
                        val drawable = (imageLoader.execute(request)).drawable

                        val palette = async {
                            Palette.Builder(drawable!!.toBitmap()).generate()
                        }

                        dominantColor = palette.await().dominantSwatch?.rgb ?: 0x000000

                        val hex = "#%06x".format(dominantColor)

                        PokedexListEntry(entry.name, url, hex)
                    }
                    currentPage += PAGE_SIZE
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
