package com.example.mahjongscroeboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mahjongscroeboard.databinding.FragmentSecondBinding
import com.example.mahjongscroeboard.db.AppDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var playerStatsAdapter: PlayerStatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            db.gameRecordDao().getPlayerStats().collectLatest {
                playerStatsAdapter.submitList(it)
            }
        }
    }

    private fun setupRecyclerView() {
        playerStatsAdapter = PlayerStatsAdapter { playerStats ->
            // TODO: Navigate to a new fragment to show details for playerStats.playerName
        }
        binding.recordsRecyclerView.apply {
            adapter = playerStatsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}