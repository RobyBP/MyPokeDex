package com.example.mypokedex.screen.pokemondetail

import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.mypokedex.R
import com.example.mypokedex.core.BaseFragment
import com.example.mypokedex.data.remote.response.Pokemon
import com.example.mypokedex.data.remote.response.Type
import com.example.mypokedex.util.Constants
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PokemonDetailFragment : BaseFragment() {

    private lateinit var pokemonData: ConstraintLayout
    private lateinit var pokemonNameAndNumber: TextView
    private lateinit var pokemonSprite: ImageView
    private lateinit var background: ConstraintLayout
    private lateinit var pokemonWeight: TextView
    private lateinit var pokemonHeight: TextView
    private lateinit var pokemonTwoTypeSurface: ConstraintLayout
    private lateinit var pokemonFirstType: EditText
    private lateinit var pokemonSecondType: EditText
    private lateinit var pokemonOneType: EditText
    private lateinit var baseHP: LinearProgressIndicator
    private lateinit var baseAttack: LinearProgressIndicator
    private lateinit var baseDefense: LinearProgressIndicator
    private lateinit var baseSpecialAttack: LinearProgressIndicator
    private lateinit var baseSpecialDefense: LinearProgressIndicator
    private lateinit var baseSpeed: LinearProgressIndicator
    private lateinit var loadingAnimation: CircularProgressIndicator

    override val model: PokemonDetailViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_pokemon_detail

    private val typeToColor = mapOf(
        "normal" to "#A8A878",
        "fighting" to "#C03028",
        "flying" to "#A890F0",
        "poison" to "#A890F0",
        "ground" to "#E0C068",
        "rock" to "#B8A038",
        "bug" to "#A8B820",
        "ghost" to "#705898",
        "steel" to "#B8B8D0",
        "fire" to "#F08030",
        "water" to "#6890F0",
        "grass" to "#78C850",
        "electric" to "#F8D030",
        "psychic" to "#F85888",
        "ice" to "#98D8D8",
        "dragon" to "#7038F8",
        "dark" to "#705848",
        "fairy" to "#EE99AC",
        "unknown" to "#68A090",
        "shadow" to "#222222"
    )

    override fun initialiseViews(view: View) {

        val name = requireArguments().getString(Constants.POKEMON_NAME_KEY)
        val spriteUrl = requireArguments().getString(Constants.POKEMON_URL_KEY)
        val dominantColor = requireArguments().getString(Constants.POKEMON_DOMINANT_COLOR_KEY)

        findViews(view)

        pokemonSprite.load(spriteUrl)
        background.setBackgroundColor(Color.parseColor(dominantColor))

        model.loadPokemon(name!!)

        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.pokemon.collect { pokemon ->
                    if (pokemon != null) {
                        loadingAnimation.visibility = View.GONE
                        pokemonData.visibility = View.VISIBLE
                        val types = pokemon.types
                        if (types.size == 1) renderSingleTypeCard(types)
                        else renderDoubleTypeCard(types)
                        renderOtherData(pokemon)
                    }
                }
            }
        }
    }

    private fun renderSingleTypeCard(type: List<Type>) {
        pokemonOneType.apply {
            visibility = View.VISIBLE
            val typeName = type[0].type.name
            setText(typeName)
            setBackgroundColor(Color.parseColor(typeToColor[typeName]))
        }
    }

    private fun renderDoubleTypeCard(types: List<Type>) {
        pokemonTwoTypeSurface.visibility = View.VISIBLE
        val firstTypeName = types[0].type.name
        val secondTypeName = types[1].type.name
        pokemonFirstType.apply {
            setText(firstTypeName)
            setBackgroundColor(Color.parseColor(typeToColor[firstTypeName]))
        }

        pokemonSecondType.apply {
            setText(secondTypeName)
            setBackgroundColor(Color.parseColor(typeToColor[secondTypeName]))
        }
    }

    private fun renderOtherData(pokemon: Pokemon) {
        pokemonHeight.text = getString(R.string.pokemon_height, pokemon.height)
        pokemonWeight.text = getString(R.string.pokemon_weight, pokemon.weight)

        pokemonNameAndNumber.text =
            getString(R.string.pokemon_detail_name_and_number, pokemon.id, pokemon.name)

        baseHP.setProgressCompat(pokemon.stats[0].base_stat, true)
        baseAttack.setProgressCompat(pokemon.stats[1].base_stat, true)
        baseDefense.setProgressCompat(pokemon.stats[2].base_stat, true)
        baseSpecialAttack.setProgressCompat(pokemon.stats[3].base_stat, true)
        baseSpecialDefense.setProgressCompat(pokemon.stats[4].base_stat, true)
        baseSpeed.setProgressCompat(pokemon.stats[5].base_stat, true)
    }

    private fun findViews(view: View) {
        pokemonData = view.findViewById(R.id.pokemon_detail_data)
        pokemonNameAndNumber = view.findViewById(R.id.pokemon_detail_name_and_number)
        pokemonSprite = view.findViewById(R.id.pokemon_detail_sprite)
        background = view.findViewById(R.id.pokemon_detail_background)
        pokemonWeight = view.findViewById(R.id.pokemon_detail_weight)
        pokemonHeight = view.findViewById(R.id.pokemon_detail_height)
        pokemonTwoTypeSurface = view.findViewById(R.id.pokemon_detail_two_type_card)
        pokemonFirstType = view.findViewById(R.id.pokemon_detail_first_type)
        pokemonSecondType = view.findViewById(R.id.pokemon_detail_second_type)
        pokemonOneType = view.findViewById(R.id.pokemon_detail_one_type_card)
        baseHP = view.findViewById(R.id.pokemon_detail_health)
        baseAttack = view.findViewById(R.id.pokemon_detail_attack)
        baseDefense = view.findViewById(R.id.pokemon_detail_defense)
        baseSpecialAttack = view.findViewById(R.id.pokemon_detail_special_attack)
        baseSpecialDefense = view.findViewById(R.id.pokemon_detail_special_defense)
        baseSpeed = view.findViewById(R.id.pokemon_detail_speed)
        loadingAnimation = view.findViewById(R.id.pokemon_detail_loading_animation)
    }
}
