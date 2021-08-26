package com.example.mypokedex.screen.pokemonlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.ImageRequest
import coil.request.ImageResult
import com.example.mypokedex.R
import com.example.mypokedex.data.PokedexListEntry

class PokemonListAdapter(private val onClick: () -> Unit) :
    ListAdapter<PokedexListEntry, PokemonListAdapter.PokemonListViewHolder>(PokemonCallback) {

    class PokemonListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonImage: ImageView = itemView.findViewById(R.id.pokemon_card_image)
        val loadingAnimation: ProgressBar = itemView.findViewById(R.id.pokemon_card_progress_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        return PokemonListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pokemon_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {

        holder.pokemonImage.load(getItem(position).imageUrl) {
            listener(object: ImageRequest.Listener {
                override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
                    super.onSuccess(request, metadata)
                    holder.loadingAnimation.visibility = View.GONE
                }
            })
        }

        holder.pokemonImage.setOnClickListener {
            onClick()
        }
    }

}

object PokemonCallback : DiffUtil.ItemCallback<PokedexListEntry>() {

    override fun areItemsTheSame(oldItem: PokedexListEntry, newItem: PokedexListEntry): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PokedexListEntry, newItem: PokedexListEntry): Boolean {
        return oldItem == newItem
    }

}
