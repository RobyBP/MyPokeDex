package com.example.mypokedex.screen.pokemonlist

import androidx.fragment.app.viewModels
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonListFragment(): BaseFragment(R.layout.fragment_pokemon_list) {

    override val model: PokemonListViewModel by viewModels()

}