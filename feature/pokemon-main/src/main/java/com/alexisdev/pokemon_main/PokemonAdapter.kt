package com.alexisdev.pokemon_main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.pokemon_main.databinding.PokemonItemBinding
import com.bumptech.glide.Glide
import java.util.Locale

class PokemonAdapter : PagingDataAdapter<Pokemon, PokemonAdapter.ViewHolder>(PokemonComparator) {

    class ViewHolder(private val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: Pokemon) {
            Glide
                .with(binding.root)
                .load(pokemon.image.imageUrl)
                //.placeholder(R.drawable.img_placeholder)
                .centerCrop()
                .into(binding.imgPokemon)
            binding.tvPokemonName.text = pokemon.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("my", getItem(position).toString())
        val pokemon = getItem(position)
        pokemon?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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