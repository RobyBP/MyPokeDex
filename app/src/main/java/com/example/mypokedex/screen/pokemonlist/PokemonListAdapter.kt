package com.example.mypokedex.screen.pokemonlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.bitmap.BitmapPool
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import coil.transform.Transformation
import com.example.mypokedex.R
import com.example.mypokedex.data.PokedexListEntry

class PokemonListAdapter(private val context: Context, private val onClick: () -> Unit) :
    ListAdapter<PokedexListEntry, PokemonListAdapter.PokemonListViewHolder>(PokemonCallback) {

    class PokemonListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonImage: ImageView = itemView.findViewById(R.id.pokemon_card_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        return PokemonListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pokemon_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {

        holder.pokemonImage.load(getItem(position).imageUrl) {
            transformations(object: Transformation {
                override fun key(): String = "paletteTransformer"

                override suspend fun transform(
                    pool: BitmapPool,
                    input: Bitmap,
                    size: Size
                ): Bitmap {
                    val palette = Palette.from(input).generate()
                    holder.pokemonImage.setBackgroundColor(palette.getDominantColor(0xFFFFFF))
                    return input
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
