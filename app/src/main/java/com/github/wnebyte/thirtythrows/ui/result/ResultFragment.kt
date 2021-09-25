package com.github.wnebyte.thirtythrows.ui.result

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.wnebyte.thirtythrows.databinding.FragmentResultBinding
import java.lang.IllegalStateException

class ResultFragment : Fragment() {

    interface Callbacks {
        fun onNewGame()
    }

    private val args: ResultFragmentArgs by navArgs()

    private val vm: ResultViewModel by viewModels()

    private var _binding: FragmentResultBinding? = null

    private val binding get() = _binding!!

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callbacks = context as Callbacks
        } catch (e: Exception) {
            throw IllegalStateException(
                    "hosting activity needs to implements Callbacks interface"
            )
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(layoutInflater, container, false)
        binding.button.setOnClickListener(onButtonPressed())
        updateUI()
        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun updateUI() {
        var i = 1
        for (round in args.rounds) {
            val row = bind(i++, round.system, round.score)
            binding.body.addView(row)
        }
        val sum: Int = args.rounds.sumOf { r -> r.score }
        val row = bind("", "", sum)
        binding.body.addView(row)
    }

    private fun onButtonPressed(): View.OnClickListener {
        return View.OnClickListener {
            callbacks?.onNewGame()
        }
    }

    private fun bind(vararg it: Any): TableRow {
        val row = TableRow(requireContext())
        row.setPadding(10)
        row.gravity = Gravity.CENTER
        for (item in it) {
            val tv = TextView(requireContext())
            tv.text = item.toString()
            tv.gravity = Gravity.CENTER
            tv.textSize = 16f
            row.addView(tv)
        }
        return row
    }
}