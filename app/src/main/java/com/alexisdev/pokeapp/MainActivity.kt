package com.alexisdev.pokeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.alexisdev.common.getKoinInstance
import com.alexisdev.common.navigation.NavDirection
import com.alexisdev.common.navigation.NavEffect
import com.alexisdev.common.navigation.NavigationManager
import com.alexisdev.pokemon_main.PokemonCatalogFragment
import com.alexisdev.pokemon_main.PokemonCatalogFragmentDirections
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val navManager: NavigationManager = getKoinInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(findViewById(R.id.toolbar))
        navController = this.findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        observeNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun observeNavigation() {
        lifecycleScope.launch {
            navManager.navEffectFlow.collect { navEffect ->
                when (navEffect) {
                    is NavEffect.NavigateTo -> {
                        handleNavDirection(navEffect.direction, navController)
                    }

                    is NavEffect.NavigateUp -> {
                        navController.navigateUp()
                    }
                }
            }
        }
    }

    private fun handleNavDirection(navDirection: NavDirection, navController: NavController) {
        when (navDirection) {
            is NavDirection.PokemonCatalogToPokemonDetails -> {
                val action = PokemonCatalogFragmentDirections.actionPokemonCatalogFragmentToPokemonDetailsFragment(navDirection.pokeName)
                navController.navigate(action)
            }
        }
    }
}