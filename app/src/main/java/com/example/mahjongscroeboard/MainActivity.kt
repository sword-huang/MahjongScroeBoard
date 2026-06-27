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
import androidx.lifecycle.Lifecycle
import com.example.mahjongscroeboard.databinding.ActivityMainBinding
import com.example.mahjongscroeboard.ui.PlayerStatsFragment
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var currentDestinationId: Int? = null
    private var pendingStatsMenuAction: StatsMenuAction? = null

    private enum class StatsMenuAction {
        EXPORT,
        IMPORT,
        CLEAR
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

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
                    R.id.action_export_records -> {
                        dispatchToPlayerStats(StatsMenuAction.EXPORT)
                    }
                    R.id.action_import_records -> {
                        dispatchToPlayerStats(StatsMenuAction.IMPORT)
                    }
                    R.id.action_clear_records -> {
                        dispatchToPlayerStats(StatsMenuAction.CLEAR)
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.STARTED)

        currentDestinationId = navController.currentDestination?.id
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestinationId = destination.id
            if (destination.id == R.id.player_stats_fragment) {
                binding.root.post { performPendingStatsMenuAction() }
            }
            invalidateMenu()
        }
    }

    private fun getVisiblePlayerStatsFragment(): PlayerStatsFragment? {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as? NavHostFragment
        return navHostFragment?.childFragmentManager?.primaryNavigationFragment as? PlayerStatsFragment
    }

    private fun navigateToPlayerStats(): Boolean {
        if (currentDestinationId == R.id.player_stats_fragment) {
            return true
        }
        findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.player_stats_fragment)
        return true
    }

    private fun dispatchToPlayerStats(action: StatsMenuAction): Boolean {
        val visibleFragment = getVisiblePlayerStatsFragment()
        if (visibleFragment != null) {
            return runStatsMenuAction(visibleFragment, action)
        }

        pendingStatsMenuAction = action
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        if (currentDestinationId != R.id.player_stats_fragment) {
            navController.navigate(R.id.player_stats_fragment)
        } else {
            binding.root.post { performPendingStatsMenuAction() }
        }
        return true
    }

    private fun performPendingStatsMenuAction() {
        val pendingAction = pendingStatsMenuAction ?: return
        val fragment = getVisiblePlayerStatsFragment() ?: return
        if (runStatsMenuAction(fragment, pendingAction)) {
            pendingStatsMenuAction = null
        }
    }

    private fun runStatsMenuAction(fragment: PlayerStatsFragment, action: StatsMenuAction): Boolean {
        return when (action) {
            StatsMenuAction.EXPORT -> fragment.onExportRecordsMenuClicked()
            StatsMenuAction.IMPORT -> fragment.onImportRecordsMenuClicked()
            StatsMenuAction.CLEAR -> fragment.onClearRecordsMenuClicked()
        }
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