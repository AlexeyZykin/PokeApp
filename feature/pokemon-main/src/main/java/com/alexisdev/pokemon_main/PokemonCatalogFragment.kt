package com.alexisdev.pokemon_main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexisdev.pokemon_main.databinding.FragmentPokemonCatalogBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PokemonCatalogFragment : Fragment() {
    private lateinit var binding: FragmentPokemonCatalogBinding;
    private val viewModel by viewModel<PokemonCatalogViewModel>()
    private var currentSnackbar: Snackbar? = null
    private val adapter by lazy { PokemonAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observePokemonCatalogState()
    }

    private fun observePokemonCatalogState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is PokemonCatalogState.Loading -> {
                        }
                        is PokemonCatalogState.Content -> {
                            currentSnackbar?.dismiss()
                            adapter.submitData(lifecycle, state.pagingData)
                        }
                    }
                }
            }
        }
    }

    private fun showProgressBar(isShow: Boolean) {
        if (isShow) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE

    }

    private fun showBottomProgressBar(isShow: Boolean) {
        if (isShow) binding.progressBottom.visibility = View.VISIBLE
        else binding.progressBottom.visibility = View.GONE
    }

    private fun showErrorSnackbar(view: View, msg: String) {
        val snackbar = Snackbar.make(
            view, msg, Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Повторить") {
            viewModel.onEvent(PokemonCatalogEvent.OnRetry)
        }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(snackbar: Snackbar, event: Int) {
                super.onDismissed(snackbar, event)
                if (event == DISMISS_EVENT_SWIPE) {
                    showErrorSnackbar(view, msg)
                }
            }
        })
        currentSnackbar = snackbar
        snackbar.show()
    }

    private fun initRecyclerView() {
        binding.rvPokemons.layoutManager = LinearLayoutManager(context)
        binding.rvPokemons.adapter = adapter
        observePagingLoadState()
    }

    private fun observePagingLoadState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect { loadState ->

                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            showProgressBar(true)
                        }
                        is LoadState.Error -> {
                            showErrorSnackbar(binding.root, "Произошла ошибка при загрузке")
                        }
                        else -> {
                            showProgressBar(false)
                        }
                    }

                    when (loadState.append) {
                        is LoadState.Loading -> {
                            showBottomProgressBar(true)
                        }

                        is LoadState.Error -> {}

                        is LoadState.NotLoading -> {
                            showBottomProgressBar(false)
                        }
                    }
                }
            }
        }
    }

}

