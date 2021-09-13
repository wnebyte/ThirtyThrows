package com.github.wnebyte.thirtythrows.controller

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.wnebyte.thirtythrows.R
import com.github.wnebyte.thirtythrows.logic.GameLogic
import com.github.wnebyte.thirtythrows.model.Die
import com.github.wnebyte.thirtythrows.model.DieListViewModel
import java.lang.IllegalArgumentException

private const val TAG = "DieListFragment"

private const val DIE_TAG = "Die"

class DieListFragment : Fragment() {

    private lateinit var dieRecyclerView: RecyclerView

    private var adapter: DieAdapter? = null

    /** Fragment's and MainActivity's shared view model. */
    private val model: DieListViewModel by lazy {
        ViewModelProviders.of(this.requireActivity()).get(DieListViewModel::class.java)
    }

    private lateinit var game: GameLogic

    /** callback interface */
    interface EndOfRoundCallback {
        fun onEndOfRound()}


    private var callback: EndOfRoundCallback? = null

    /**
     *
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as EndOfRoundCallback
        }
        catch (e: Exception) {
            throw IllegalStateException(
                "activity needs to implement the DieListFragment.EndOfRoundCallback interface"
            )
        }
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

    /**
     * OnCreates instantiates this class's lateinit GameLogic var.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        game = GameLogic(model)
    }

    /**
     * OnCreateView inflates the view, initializes this class's RecyclerView,
     * and sets it layout manager.
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_die_list, container, false)
        dieRecyclerView = view.findViewById(R.id.die_recycler_view) as RecyclerView
        dieRecyclerView.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        updateUI()
        return view
    }

    /**
     * Re-instantiates the DieAdapter class, which in turn re-binds the dice to the their views.
     */
    private fun updateUI() {
        val dice = model.dice
        adapter = DieAdapter(dice)
        dieRecyclerView.adapter = adapter
    }

    /**
     *
     */
    private inner class DieHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var die: Die
        private val imageView: ImageView = itemView.findViewById(R.id.die_image) as ImageView

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(die: Die) {
            this.die = die
            val resourceId: Int
            val backgroundId: Int

            when (die.number) {
                1 -> {
                    resourceId = when(die.throwAgain) {
                        true -> R.drawable.die_white_1
                        false -> R.drawable.die_grey_1
                    }
                }
                2 -> {
                    resourceId = when(die.throwAgain) {
                        true -> R.drawable.die_white_2
                        false -> R.drawable.die_grey_2
                    }
                }
                3 -> {
                    resourceId = when(die.throwAgain) {
                        true -> R.drawable.die_white_3
                        false -> R.drawable.die_grey_3
                    }
                }
                4 -> {
                    resourceId = when(die.throwAgain) {
                        true -> R.drawable.die_white_4
                        false -> R.drawable.die_grey_4
                    }
                }
                5 -> {
                    resourceId = when(die.throwAgain) {
                        true -> R.drawable.die_white_5
                        false -> R.drawable.die_grey_5
                    }
                }
                6 -> {
                    resourceId = when(die.throwAgain) {
                        true -> R.drawable.die_white_6
                        false -> R.drawable.die_grey_6
                    }
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }

            when (die.group) {
                0 -> {
                    backgroundId = Color.TRANSPARENT
                }
                1 -> {
                    backgroundId = Color.BLUE
                }
                2 -> {
                    backgroundId = Color.GREEN
                }
                3 -> {
                    backgroundId = Color.YELLOW
                }
                4 -> {
                    backgroundId = Color.RED
                }
                5 -> {
                    backgroundId = Color.CYAN
                }
                6 -> {
                    backgroundId = Color.DKGRAY
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }

            imageView.setImageResource(resourceId)
            imageView.setBackgroundColor(backgroundId)
        }

        /**
         * model.Die onClick handler toggles the throwAgain, or the group property of the clicked die,
         * and then re-binds the die.
         */
        override fun onClick(v: View?) {
            if (!game.endOfRound())
            {
                die.throwAgain = !die.throwAgain
            }
            else
            {
                die.toggleGroup()
            }
            bind(die)
        }
    }

    /**
     *
     */
    private inner class DieAdapter(var dice: List<Die>): RecyclerView.Adapter<DieHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DieHolder {
            val view = layoutInflater.inflate(R.layout.list_item_die, parent, false)
            return DieHolder(view)
        }

        override fun getItemCount(): Int {
            return dice.size
        }

        override fun onBindViewHolder(holder: DieHolder, position: Int) {
            val die = dice[position]
            holder.bind(die)
        }

    }

    companion object {
        /**
         * Constructs and returns a new instance of this class.
         */
        fun newInstance(): DieListFragment {
            return DieListFragment()
        }
    }

    /**
     * If the underlying properties of the shared view model indicate that the game is done, then
     * the score for the last round is recorded and this function returns true. On the other hand,
     * if the model indicate that three throws have been made, then the callback function is called,
     * and the score for the previous round is logged, the model's dice are cleared, the UI is
     * updated, and a toast is shown, and the value false is returned. In all other cases, the shared
     * view model's dice list property will have new dice added based on the properties of the existing die,
     * and the UI will be updated, a toast will be shown, and the value false will be returned.
     */
    fun throwDice(): Boolean {
        if (game.done())
        {
            callback?.onEndOfRound()
            game.logScore()
            return true
        }

        if (game.endOfRound())
        {
            if (model.dice.filter { d -> d.group == 0 }.size == 6)
            {
                return false
            }

            callback?.onEndOfRound()
            game.logScore()
            game.clearDice()
            game.nextRound()
            updateUI()
            return false
        }
        game.play()
        updateUI()
        return false
    }
}
