package com.example.mypokedex.screen.pokemonlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import com.example.mypokedex.data.PokedexListEntry
import com.example.mypokedex.util.Constants
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PokemonListFragment : BaseFragment() {

    override val model: PokemonListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: CircularProgressIndicator

    override val layoutId: Int = R.layout.fragment_pokemon_list

    override fun initialiseViews(view: View) {
        recyclerView = view.findViewById(R.id.pokemon_list_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        progressBar = view.findViewById(R.id.pokemon_list_progress_bar)

        val adapter = PokemonListAdapter(object : (PokedexListEntry) -> Unit {
            override fun invoke(pokemon: PokedexListEntry) {
                val arguments = Bundle()
                arguments.putString(Constants.POKEMON_NAME_KEY, pokemon.pokemonName)
                arguments.putString(Constants.POKEMON_URL_KEY, pokemon.imageUrl)
                arguments.putString(Constants.POKEMON_DOMINANT_COLOR_KEY, pokemon.dominantColorHex)

                findNavController().navigate(
                    R.id.action_pokemonListFragment_to_pokemonDetailFragment,
                    arguments
                )
            }

        })

        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !model.isLoading.value) model.loadPokemonPaginated()
            }
        })

        lifecycleScope.launchWhenStarted {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                model.isLoading.collectLatest { isLoading ->
                    if (!isLoading) {
                        progressBar.visibility = View.GONE
                        model.pokemonList.collect {
                            adapter.submitList(it)
                        }
                    } else progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}
