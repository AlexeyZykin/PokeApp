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
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.model.StatType
import com.alexisdev.pokemon_main.databinding.FragmentPokemonCatalogBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.alexisdev.common.R as designsystem


class PokemonCatalogFragment : Fragment() {
    private var _binding: FragmentPokemonCatalogBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PokemonCatalogViewModel>()
    private var currentSnackbar: Snackbar? = null
    private val adapter by lazy { PokemonAdapter(object : PokemonAdapter.ClickListener {
        override fun onClick(pokeName: String) {
            viewModel.onEvent(PokemonCatalogEvent.OnNavigateToDetails(pokeName))
        }
    }) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observePokemonCatalogState()
        observePagingLoadState()
    }


    private fun observePokemonCatalogState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is PokemonCatalogState.Loading -> Unit
                        is PokemonCatalogState.Content -> {
                            Log.d("PokeTest", "Content trigger")
                            handleContentState(state.pagingData)
                        }
                    }
                }
            }
        }
    }


    private fun handleContentState(pagingData: PagingData<Pokemon>) {
        adapter.submitData(lifecycle, pagingData)
        setupCheckBoxes()
        observeTopPokemonUpdates()
        binding.btnPokemonsReinitialization.setOnClickListener {
            viewModel.onEvent(PokemonCatalogEvent.OnReinitialize)
            resetCheckBoxes()
        }
    }


    private fun observeTopPokemonUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topPokemonUpdateState.collectLatest {
                delay(300)
                withContext(Dispatchers.Main) {
                    binding.rvPokemons.smoothScrollToPosition(START_SCROLL_POSITION)
                }
            }
        }
    }


    private fun resetCheckBoxes() = with(binding) {
        listOf(checkboxAttack, checkboxDefense, checkboxHp).forEach { checkBox ->
            checkBox.isChecked = false
        }
    }


    private fun setupCheckBoxes() {
        binding.checkboxAttack.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onEvent(PokemonCatalogEvent.OnCheckStatFilter(StatType.ATTACK, isChecked))
        }
        binding.checkboxDefense.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onEvent(PokemonCatalogEvent.OnCheckStatFilter(StatType.DEFENSE, isChecked))
        }
        binding.checkboxHp.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onEvent(PokemonCatalogEvent.OnCheckStatFilter(StatType.HP, isChecked))
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
        snackbar.setAction(getString(R.string.error_action_retry)) {
            viewModel.onEvent(PokemonCatalogEvent.OnRetry)
            resetCheckBoxes()
        }
        snackbar.setActionTextColor(requireContext().getColor(designsystem.color.pink))
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


    private fun setupRecyclerView() {
        binding.rvPokemons.layoutManager = LinearLayoutManager(context)
        binding.rvPokemons.adapter = adapter
    }


    private fun observePagingLoadState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect { loadState ->

                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            currentSnackbar?.dismiss()
                            showProgressBar(true)
                        }
                        is LoadState.Error -> {
                            showProgressBar(false)
                            showErrorSnackbar(binding.root, getString(R.string.error_message_title))
                        }
                        is LoadState.NotLoading  -> {
                            showProgressBar(false)
                        }
                    }

                    when (loadState.append) {
                        is LoadState.Loading -> {
                            showBottomProgressBar(true)
                        }

                        is LoadState.Error -> {
                            showBottomProgressBar(false)
                        }

                        is LoadState.NotLoading -> {
                            showBottomProgressBar(false)
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val START_SCROLL_POSITION = 0
    }
}

