package com.example.mypokedex.screen.pokemonlist

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonListFragment(): BaseFragment() {

    override val model: PokemonListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView

    override val layoutId: Int = R.layout.fragment_pokemon_list

    override fun initialiseViews(view: View) {
        recyclerView = view.findViewById(R.id.pokemon_list_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = PokemonListAdapter { }
        recyclerView.adapter = adapter
        adapter.submitList(listOf())

    }
}
