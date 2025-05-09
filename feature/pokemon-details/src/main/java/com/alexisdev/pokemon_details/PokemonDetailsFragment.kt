package com.alexisdev.pokemon_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alexisdev.domain.model.PokemonDetails
import com.alexisdev.pokemon_details.databinding.FragmentPokemonDetailsBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class PokemonDetailsFragment : Fragment() {
    private var _binding: FragmentPokemonDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PokemonDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is PokemonDetailsState.Loading -> {

                        }
                        is PokemonDetailsState.Error -> {

                        }
                        is PokemonDetailsState.Content -> {
                            renderUi(state.pokemonDetails)
                        }
                    }
                }
            }
        }
    }

    private fun renderUi(pokemonDetails: PokemonDetails) = with (binding) {
        Log.d("PokeTest", pokemonDetails.toString())
        tvPokemonName.text = pokemonDetails.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
        tvPokeHeight.text = pokemonDetails.height.toString()
        tvPokeWeight.text = pokemonDetails.weight.toString()
        tvPokeType.text = pokemonDetails.types.joinToString(separator = ", ") { it.type }

        pokemonDetails.stats.forEach { stat ->
            val statType = stat.toStatType(stat.stat.name)
            when (statType) {
                StatType.ATTACK -> tvPokeStatsAttack.text = stat.baseStat.toString()
                StatType.DEFENSE -> tvPokeStatsDefense.text = stat.baseStat.toString()
                StatType.HP -> tvPokeStatsHp.text = stat.baseStat.toString()
                else -> {}
            }
        }

        Glide.with(requireContext())
            .load(pokemonDetails.image.imageUrl)
            .centerCrop()
            .into(imgPokemon)

        listOf(labelGeneral, labelPokeStats, cardPokeStats, cardGeneralPokeInfo, cardPokeImg).forEach {
            it.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}