package com.github.wnebyte.thirtythrows.ui.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.wnebyte.thirtythrows.model.Die
import com.github.wnebyte.thirtythrows.model.Round

private const val DICE_STATE_KEY = "Dice"
private const val ROUNDS_STATE_KEY = "Rounds"
private const val THROWS_KEY = "Throws"
private const val SPINNER_ITEMS_KEY = "SpinnerItems"
private const val SELECTED_SPINNER_ITEM_KEY = "SelectedSpinnerItem"

class GameViewModel(private val state: SavedStateHandle)
    : ViewModel() {

    private var _dice: ArrayList<Die> =
            state.get<ArrayList<Die>>(DICE_STATE_KEY) ?: initDice()

    val dice: ArrayList<Die> get() = _dice

    private var _rounds: ArrayList<Round> =
            state.get<ArrayList<Round>>(ROUNDS_STATE_KEY) ?: initRounds()

    val rounds get() = _rounds

    private var _throws: Int = state.get<Int>(THROWS_KEY) ?: initThrows()

    val throws get() = _throws

    var selectedItem: Int = state.get<Int>(SELECTED_SPINNER_ITEM_KEY) ?: initSelectedItem()

    var spinnerItems: ArrayList<Int> =
            state.get<ArrayList<Int>>(SPINNER_ITEMS_KEY) ?: initSpinnerItems()

    private fun initDice(): ArrayList<Die> {
        val dice: ArrayList<Die> = arrayListOf()
        state.set(DICE_STATE_KEY, dice)
        return dice
    }

    private fun initRounds(): ArrayList<Round> {
        val rounds: ArrayList<Round> = arrayListOf()
        state.set(ROUNDS_STATE_KEY, rounds)
        return rounds
    }

    private fun initThrows(): Int {
        val throws = 0
        state.set(THROWS_KEY, throws)
        return throws
    }

    private fun initSelectedItem(): Int {
        val selectedItem = 3
        state.set(SELECTED_SPINNER_ITEM_KEY, selectedItem)
        return selectedItem
    }

    private fun initSpinnerItems(): ArrayList<Int> {
        val spinnerItems: ArrayList<Int> = arrayListOf(
                3, 4, 5, 6, 7, 8, 9, 10, 11, 12
        )
        state.set(SPINNER_ITEMS_KEY, spinnerItems)
        return spinnerItems
    }

    /**
     * Increments `throws` and returns the result.
     * @return the incremented `throws`.
     */
    fun incrementThrows(): Int {
        _throws = when (throws) {
            MAX_THROWS -> 0
            else -> ++_throws
        }
        return throws
    }

    /**
     * Returns whether the game is over.
     * @return `true` if the game is over,
     * otherwise `false`.
     */
    fun isEndOfGame(): Boolean {
        return (rounds.size == MAX_ROUND) && (throws == MAX_THROWS)
    }

    /**
     * Returns whether the round is over.
     * @return `true` if the round is over,
     * otherwise `false`.
     */
    fun isEndOfRound(): Boolean {
        return (throws == MAX_THROWS)
    }

    /**
     * Calculates and saves the score for the current round.
     * @return the score.
     */
    fun nextRound(): Int {
        val round = Round.newInstance(dice.toSet(), selectedItem)
        rounds.add(round)
        return round.score
    }

    /**
     * Updates this ViewModel's internal persistent state.
     */
    fun saveViewModelState() {
        state[DICE_STATE_KEY] = dice
        state[ROUNDS_STATE_KEY] = rounds
        state[THROWS_KEY] = throws
        state[SELECTED_SPINNER_ITEM_KEY] = selectedItem
        state[SPINNER_ITEMS_KEY] = spinnerItems
    }

    /**
     * Removes any internal persistent state of this ViewModel.
     */
    fun destroyViewModelState() {
        state.remove<ArrayList<Die>>(DICE_STATE_KEY)
        state.remove<ArrayList<Round>>(ROUNDS_STATE_KEY)
        state.remove<Int>(THROWS_KEY)
        state.remove<Int>(SELECTED_SPINNER_ITEM_KEY)
        state.remove<ArrayList<Int>>(SPINNER_ITEMS_KEY)
    }

    companion object {

        /**
         * The number of rounds in a game.
         */
        const val MAX_ROUND: Int = 10

        /**
         * The number of throws in a round.
         */
        const val MAX_THROWS: Int = 3
    }
}