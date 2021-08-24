package com.example.mypokedex.screen.pokemonlist

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PokemonListFragment : BaseFragment() {

    override val model: PokemonListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView

    override val layoutId: Int = R.layout.fragment_pokemon_list

    override fun initialiseViews(view: View) {
        recyclerView = view.findViewById(R.id.pokemon_list_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = PokemonListAdapter { }
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) model.loadPokemonPaginated()
            }
        })

        lifecycleScope.launchWhenStarted {
            model.pokemonList.collect {
                adapter.submitList(it)
            }
        }
    }
}
