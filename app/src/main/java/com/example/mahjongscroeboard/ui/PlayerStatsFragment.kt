package com.example.mahjongscroeboard.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mahjongscroeboard.R
import com.example.mahjongscroeboard.databinding.FragmentPlayerStatsBinding

class PlayerStatsFragment : Fragment() {

    private var _binding: FragmentPlayerStatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlayerStatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_player_stats, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_records -> {
                showClearConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
