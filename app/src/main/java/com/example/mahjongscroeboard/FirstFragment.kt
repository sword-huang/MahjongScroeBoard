package com.example.mahjongscroeboard

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.fragment.findNavController
import com.example.mahjongscroeboard.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val history = mutableListOf<GameState>()
    private var historyIndex = -1

    data class GameState(val buttonTexts: List<String>)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttons = listOf(
            binding.button22, binding.button23, binding.button24, binding.button25,
            binding.button32, binding.button33, binding.button34, binding.button35,
            binding.button42, binding.button43, binding.button44, binding.button45,
            binding.button52, binding.button53, binding.button54, binding.button55
        )

        buttons.forEach { button ->
            setupButton(button)
        }

        val selfDrawButtons = listOf(
            binding.button22,
            binding.button33,
            binding.button44,
            binding.button55
        )

        val column1 = listOf(binding.textview11, binding.textview21, binding.textview31, binding.textview41, binding.textview51, binding.textview61)
        val column2 = listOf(binding.textview12, binding.button22, binding.button32, binding.button42, binding.button52, binding.total2)
        val column3 = listOf(binding.textview13, binding.button23, binding.button33, binding.button43, binding.button53, binding.total3)
        val column4 = listOf(binding.textview14, binding.button24, binding.button34, binding.button44, binding.button54, binding.total4)
        val column5 = listOf(binding.textview15, binding.button25, binding.button35, binding.button45, binding.button55, binding.total5)

        val columns = listOf(column1, column2, column3, column4, column5)
        val colors = listOf(Color.TRANSPARENT, Color.LTGRAY, Color.CYAN, Color.YELLOW, Color.MAGENTA)
        columns.forEachIndexed { index, column ->
            val color = colors[index % colors.size]
            column.forEach { view ->
                val background = ContextCompat.getDrawable(requireContext(), R.drawable.colored_background)?.mutate()
                background?.let {
                    DrawableCompat.setTint(it, color)
                    view.background = it
                }
            }
        }

        selfDrawButtons.forEach { button ->
            val background = ContextCompat.getDrawable(requireContext(), R.drawable.colored_background)?.mutate()
            background?.let {
                DrawableCompat.setTint(it, Color.RED)
                button.background = it
            }
        }
        saveState()
        updateTotals()

        binding.undoButton.setOnClickListener {
            if (historyIndex > 0) {
                historyIndex--
                restoreState(history[historyIndex])
            }
        }

        binding.redoButton.setOnClickListener {
            if (historyIndex < history.size - 1) {
                historyIndex++
                restoreState(history[historyIndex])
            }
        }

        binding.clearButton.setOnClickListener {
            buttons.forEach { button ->
                button.text = "0"
            }
            saveState()
            updateTotals()
        }
    }

    private fun setupButton(button: Button) {
        button.setOnClickListener {
            val currentValue = button.text.toString().toInt()
            button.text = (currentValue + 1).toString()
            saveState()
            updateTotals()
        }
    }

    private fun saveState() {
        val buttonTexts = mutableListOf<String>()
        val buttons = listOf(
            binding.button22, binding.button23, binding.button24, binding.button25,
            binding.button32, binding.button33, binding.button34, binding.button35,
            binding.button42, binding.button43, binding.button44, binding.button45,
            binding.button52, binding.button53, binding.button54, binding.button55
        )
        buttons.forEach { button ->
            buttonTexts.add(button.text.toString())
        }
        if (historyIndex < history.size - 1) {
            history.subList(historyIndex + 1, history.size).clear()
        }
        history.add(GameState(buttonTexts))
        historyIndex++
    }

    private fun restoreState(gameState: GameState) {
        val buttons = listOf(
            binding.button22, binding.button23, binding.button24, binding.button25,
            binding.button32, binding.button33, binding.button34, binding.button35,
            binding.button42, binding.button43, binding.button44, binding.button45,
            binding.button52, binding.button53, binding.button54, binding.button55
        )
        buttons.forEachIndexed { index, button ->
            button.text = gameState.buttonTexts[index]
        }
        updateTotals()
    }

    private fun updateTotals() {
        val total2 = binding.button22.text.toString().toInt() + binding.button32.text.toString().toInt() + binding.button42.text.toString().toInt() + binding.button52.text.toString().toInt()
        val total3 = binding.button23.text.toString().toInt() + binding.button33.text.toString().toInt() + binding.button43.text.toString().toInt() + binding.button53.text.toString().toInt()
        val total4 = binding.button24.text.toString().toInt() + binding.button34.text.toString().toInt() + binding.button44.text.toString().toInt() + binding.button54.text.toString().toInt()
        val total5 = binding.button25.text.toString().toInt() + binding.button35.text.toString().toInt() + binding.button45.text.toString().toInt() + binding.button55.text.toString().toInt()

        binding.total2.text = total2.toString()
        binding.total3.text = total3.toString()
        binding.total4.text = total4.toString()
        binding.total5.text = total5.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}