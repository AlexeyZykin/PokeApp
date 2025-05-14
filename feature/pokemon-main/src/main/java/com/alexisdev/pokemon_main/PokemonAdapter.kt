package com.alexisdev.pokemon_main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.pokemon_main.databinding.PokemonItemBinding
import com.bumptech.glide.Glide
import java.util.Locale
import com.alexisdev.common.R as designsystem

class PokemonAdapter(private val clickListener: ClickListener) : PagingDataAdapter<Pokemon, PokemonAdapter.ViewHolder>(PokemonComparator) {


    inner class ViewHolder(private val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: Pokemon, clickListener: ClickListener) {
            val context = binding.root.context

            val colorRes = if (pokemon.isTop) designsystem.color.primary
            else designsystem.color.white
            binding.cvPokemon.setCardBackgroundColor(context.getColor(colorRes))

            binding.tvPokeId.text = pokemon.id.toString()
            Glide
                .with(binding.root)
                .load(pokemon.image.imageUrl)
                .centerCrop()
                .into(binding.imgPokemon)
            binding.tvPokemonName.text = pokemon.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
            binding.root.setOnClickListener {
                clickListener.onClick(pokemon.name)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = getItem(position)
        pokemon?.let {
            holder.bind(it, clickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    interface ClickListener {
        fun onClick(pokeName: String)
    }

    object PokemonComparator : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }
}