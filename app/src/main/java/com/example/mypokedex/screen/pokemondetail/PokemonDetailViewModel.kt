package com.example.mypokedex.screen.pokemondetail

import androidx.lifecycle.viewModelScope
import com.example.mypokedex.core.BaseViewModel
import com.example.mypokedex.data.remote.response.Pokemon
import com.example.mypokedex.database.PokemonRepository
import com.example.mypokedex.util.NetworkViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(private val repository: PokemonRepository) :
    BaseViewModel() {

    val pokemon = MutableStateFlow<Pokemon?>(null)
    private val error = MutableStateFlow<String>("")
    private val isLoading = MutableStateFlow(false)

    fun loadPokemon(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            when (val result = repository.getPokemonDetail(name)) {
                is NetworkViewState.Success -> {
                    isLoading.value = false
                    pokemon.value = result.data
                }
                is NetworkViewState.Error -> {
                    isLoading.value = false
                    error.value = result.message!!
                }
            }
        }
    }
}