package com.github.wnebyte.thirtythrows.logic

import com.github.wnebyte.thirtythrows.model.Die
import com.github.wnebyte.thirtythrows.model.DieListViewModel

/** Class for querying and modifying the shared view model */
class GameLogic(private val model: DieListViewModel) {

    /** Returns whether ten complete rounds of play have been finished. */
    fun done(): Boolean {
        return model.round == 10 && model.throws == 3
    }

    /** Returns whether the end of a round has been reached. */
    fun endOfRound(): Boolean {
        return model.throws == 3
    }

    /** Increments the rounds. */
    fun nextRound() {
        model.throws = 0
        model.round++
    }

    private fun nextThrow() = model.throws++

    /** Clears the dice. */
    fun clearDice() {
        model.dice.clear()
    }

    /** Adds six new die to the model if the model is empty, otherwise replaces all of the die with
     * a true value for their throwAgain property, and sets the value for the property to true for all
     * of the remaining die. Also increments the model.throws property. */
    fun play() {
        if (model.isEmpty()) {
            for (i in 1 until 7) {
                val die = Die()
                model.dice.add(die)
            }
        } else {
            for (i in 0 until model.dice.size) {
                if (model.dice[i].throwAgain) {
                    model.dice[i] = Die()
                }
                else {
                    model.dice[i].throwAgain = true
                }
            }
        }
        nextThrow()
    }

    /**
     * Logs the score for the model's currently selected scoring system, and stores the result back
     * into the model.
     */
    fun logScore(): Int? {
        val ss = model.currentScoringSystem
        val matrix: MutableList<List<Die>> = mutableListOf()

        for (i in 1..6)
        {
            matrix.add(model.dice.filter { d -> d.group == i })
        }

        model.score[ss] = when(ss.number) {
            3 -> low(model.dice)
            else -> score(matrix, ss.number)
        }

        return model.score[ss]
    }

    /**
     * Calculates the score for a round of throws where the selected scoring system is 'LOW'.
     */
    private fun low(dice: List<Die>): Int {
        var score = 0

        for (die in dice) {
            if (die.number <= 3) {
                score += die.number
            }
        }

        return score
    }

    /**
     * Calculates the score for a round of throws where the selected scoring system is not 'LOW'.
     */
    private fun score(matrix: List<List<Die>>, targetSum: Int): Int {
        return matrix.count { list -> list.sumBy { d -> d.number } == targetSum } * targetSum
    }
}