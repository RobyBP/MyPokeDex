package com.example.mypokedex.screen.pokemonlist

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonListFragment(): BaseFragment() {

    override val model: PokemonListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView

    override val layoutId: Int = R.layout.fragment_pokemon_list

    override fun initialiseViews(view: View) {
        recyclerView = view.findViewById(R.id.pokemon_list_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = PokemonListAdapter(requireContext()) { }
        recyclerView.adapter = adapter

        MainScope().launch() {

            model.pokemonList.collect {
                adapter.submitList(it)
            }

        }
    }
}
