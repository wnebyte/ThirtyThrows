package com.github.wnebyte.thirtythrows.ui.game

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.wnebyte.thirtythrows.R
import com.github.wnebyte.thirtythrows.databinding.DieBinding
import com.github.wnebyte.thirtythrows.databinding.FragmentGameBinding
import com.github.wnebyte.thirtythrows.ext.ArrayListExt.Companion.addAll
import com.github.wnebyte.thirtythrows.model.Die
import com.github.wnebyte.thirtythrows.model.Round
import java.lang.IllegalArgumentException

private const val TAG = "GameFragment"

class GameFragment : Fragment() {

    interface Callbacks {
        fun onLastThrow(rounds: Array<Round>)
    }

    private var _binding: FragmentGameBinding? = null

    private val binding get() = _binding!!

    private val vm: GameViewModel by viewModels()

    private val adapter = DieAdapter()

    private lateinit var spinnerAdapter: ArrayAdapter<Int>

    private var callbacks: Callbacks? = null

    private var saveState: Boolean = true

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
        _binding = FragmentGameBinding
                .inflate(layoutInflater, container, false)
        binding.dieRecyclerView.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        binding.dieRecyclerView.adapter = adapter
        spinnerAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, vm.spinnerItems)
        spinnerAdapter.setNotifyOnChange(true)
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.onItemSelectedListener = SpinnerSelectionListener()
        binding.button.setOnClickListener(onButtonPressed())
        updateUI()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        if (saveState) {
            vm.saveViewModelState()
        } else {
            vm.destroyViewModelState()
        }
        Log.i(TAG, "onStop()")
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
        val dice = vm.dice
        adapter.submitList(dice.toList())
        (resources.getString(R.string.tv_round) + (vm.rounds.size + 1).toString())
                .also { binding.roundTv.text = it }
        (resources.getString(R.string.tv_throw) + vm.throws.toString())
                .also { binding.throwTv.text = it }
        binding.button.text = when {
            vm.isEndOf() -> {
                resources.getString(R.string.btn_result)
            }
            vm.throws == GameViewModel.MAX_THROWS -> {
                resources.getString(R.string.btn_next)
            }
            else -> {
                resources.getString(R.string.btn_throw)
            }
        }
    }

    private fun onButtonPressed(): View.OnClickListener {
        return View.OnClickListener {
            when (vm.throws) {
                0 -> {
                    // roll 6 new die at the start of a round
                    vm.dice.addAll(
                            Die.newInstance(), Die.newInstance(), Die.newInstance(),
                            Die.newInstance(), Die.newInstance(), Die.newInstance()
                    )
                }
                GameViewModel.MAX_THROWS -> {
                    // start a new round
                    vm.nextRound()
                    spinnerAdapter.remove(vm.selectedItem)
                    vm.dice.clear()
                }
                else -> {
                    // re-roll the the unselected die
                    vm.dice.forEachIndexed { index, die ->
                        if (die.throwAgain) {
                            vm.dice[index] = Die.newInstance()
                        }
                    }
                }
            }
            // un-select any selected die after the last roll of a round
            if (vm.throws == 2) {
                vm.dice.forEachIndexed { index, die ->
                    vm.dice[index] = Die.newInstance(die.value)
                }
            }
            if (vm.isEndOf()) {
                saveState = false
                callbacks?.onLastThrow(vm.rounds.toTypedArray())
            }
            vm.incrementThrows()
            updateUI()
        }
    }

    private inner class SpinnerSelectionListener: AdapterView.OnItemSelectedListener {

        override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
        ) {
            val selectedItem = parent?.getItemAtPosition(position) as Int
            vm.selectedItem = selectedItem
        }

        override fun onNothingSelected(
                parent: AdapterView<*>?
        ) {
            TODO("Not yet implemented")
        }

    }

    private inner class DieHolder(private val binding: DieBinding):
            RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var die: Die

        init {
            binding.dieImage.setOnClickListener(this)
        }

        fun bind(die: Die) {
            this.die = die
            val resId: Int

            when (die.value) {
                1 -> {
                    resId = when(die.throwAgain) {
                        true -> R.drawable.die_white_1
                        false -> R.drawable.die_grey_1
                    }
                }
                2 -> {
                    resId = when(die.throwAgain) {
                        true -> R.drawable.die_white_2
                        false -> R.drawable.die_grey_2
                    }
                }
                3 -> {
                    resId = when(die.throwAgain) {
                        true -> R.drawable.die_white_3
                        false -> R.drawable.die_grey_3
                    }
                }
                4 -> {
                    resId = when(die.throwAgain) {
                        true -> R.drawable.die_white_4
                        false -> R.drawable.die_grey_4
                    }
                }
                5 -> {
                    resId = when(die.throwAgain) {
                        true -> R.drawable.die_white_5
                        false -> R.drawable.die_grey_5
                    }
                }
                6 -> {
                    resId = when(die.throwAgain) {
                        true -> R.drawable.die_white_6
                        false -> R.drawable.die_grey_6
                    }
                }
                else -> {
                    throw IllegalArgumentException(
                            "${die.value} is invalid"
                    )
                }
            }

            binding.dieImage.setImageResource(resId)
        }

        override fun onClick(v: View) {
            if (vm.throws == 3) {
                return
            }
            die.throwAgain = !die.throwAgain
            bind(die)
        }
    }

    private inner class DieAdapter : ListAdapter<Die, DieHolder>(diff) {

        override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
        ): DieHolder {
            val view = DieBinding.inflate(layoutInflater, parent, false)
            return DieHolder(view)
        }

        override fun onBindViewHolder(
                holder: DieHolder,
                position: Int
        ) {
            val die = getItem(position)
            return holder.bind(die)
        }
    }

    companion object {

        private val diff: DiffUtil.ItemCallback<Die> = object : DiffUtil.ItemCallback<Die>() {

            override fun areItemsTheSame(
                oldItem: Die,
                newItem: Die
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Die, newItem: Die): Boolean {
                return oldItem == newItem
            }
        }
    }
}