package com.example.mahjongscroeboard

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import com.example.mahjongscroeboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var currentDestinationId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root.findViewById(R.id.nav_host_fragment_content_main)) { view, insets ->
            view.updatePadding(bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom)
            insets
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: android.view.MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_about -> {
                        showAboutDialog()
                        true
                    }
                    R.id.action_view_player_stats -> {
                        navigateToPlayerStats()
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.STARTED)

        currentDestinationId = navController.currentDestination?.id
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestinationId = destination.id
            invalidateMenu()
        }
    }

    private fun navigateToPlayerStats(): Boolean {
        if (currentDestinationId == R.id.player_stats_fragment) {
            return true
        }
        findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.player_stats_fragment)
        return true
    }

    private fun showAboutDialog() {
        val version = try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "N/A"
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.action_about)
            .setMessage("版本: $version")
            .setPositiveButton("確定", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}