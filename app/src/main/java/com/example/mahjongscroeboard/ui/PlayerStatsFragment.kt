package com.example.mahjongscroeboard.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mahjongscroeboard.R
import com.example.mahjongscroeboard.databinding.FragmentPlayerStatsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayerStatsFragment : Fragment() {

    private var _binding: FragmentPlayerStatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlayerStatsViewModel

    private val exportBackupLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            exportBackup(uri)
        }
    }

    private val importBackupLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            importBackup(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PlayerStatsViewModel::class.java)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.playerStats.observe(viewLifecycleOwner) {
            stats ->
            if (stats.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
                recyclerView.adapter = PlayerStatsAdapter(stats) {
                    player ->
                    val action = PlayerStatsFragmentDirections.actionPlayerStatsFragmentToGameDetailsFragment(player.playerId)
                    findNavController().navigate(action)
                }
            }
        }

        binding.header.headerPlayerName.setOnClickListener {
            viewModel.sort(SortColumn.PLAYER_NAME)
        }

        binding.header.headerTsumoCount.setOnClickListener {
            viewModel.sort(SortColumn.TSUMO_COUNT)
        }

        binding.header.headerRonCount.setOnClickListener {
            viewModel.sort(SortColumn.RON_COUNT)
        }

        binding.header.headerTotalWins.setOnClickListener {
            viewModel.sort(SortColumn.TOTAL_WINS)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onExportRecordsMenuClicked(): Boolean {
        launchExportFilePicker()
        return true
    }

    fun onImportRecordsMenuClicked(): Boolean {
        showImportConfirmationDialog()
        return true
    }

    fun onClearRecordsMenuClicked(): Boolean {
        showClearConfirmationDialog()
        return true
    }

    private fun launchExportFilePicker() {
        val fileName = "mahjong_backup_${timestampForFileName()}.json"
        exportBackupLauncher.launch(fileName)
    }

    private fun showImportConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("匯入戰績")
            .setMessage("匯入會覆蓋目前所有戰績，確定繼續嗎？")
            .setPositiveButton("確定") { _, _ ->
                importBackupLauncher.launch(arrayOf("application/json"))
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun exportBackup(uri: Uri) {
        viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                viewModel.exportBackup(uri)
            }.onSuccess {
                Snackbar.make(requireView(), "匯出成功", Snackbar.LENGTH_SHORT).show()
            }.onFailure { e ->
                Snackbar.make(requireView(), "匯出失敗：${e.message ?: "未知錯誤"}", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun importBackup(uri: Uri) {
        viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                viewModel.importBackup(uri)
            }.onSuccess {
                Snackbar.make(requireView(), "匯入成功", Snackbar.LENGTH_SHORT).show()
            }.onFailure { e ->
                Snackbar.make(requireView(), "匯入失敗：${e.message ?: "未知錯誤"}", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun timestampForFileName(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    }

    private fun showClearConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("清除戰績")
            .setMessage("確定要清除所有戰績嗎？此操作無法復原。")
            .setPositiveButton("確定") { _, _ ->
                viewModel.clearAllRecords()
            }
            .setNegativeButton("取消", null)
            .show()
    }
}
