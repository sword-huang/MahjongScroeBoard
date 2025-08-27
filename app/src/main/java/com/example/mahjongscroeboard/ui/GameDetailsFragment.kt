package com.example.mahjongscroeboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mahjongscroeboard.databinding.FragmentGameDetailsBinding

class GameDetailsFragment : Fragment() {

    private var _binding: FragmentGameDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GameDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playerId = arguments?.getInt("playerId") ?: -1
        if (playerId == -1) {
            // Handle error
            return
        }

        val factory = GameDetailsViewModelFactory(requireActivity().application, playerId)
        viewModel = ViewModelProvider(this, factory).get(GameDetailsViewModel::class.java)

        val recyclerView = binding.recyclerViewDetails
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.gameRecords.observe(viewLifecycleOwner) {
            records ->
            recyclerView.adapter = GameDetailsAdapter(records)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
