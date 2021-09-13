package com.github.wnebyte.thirtythrows.model

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize

/** ViewModel class */
@Parcelize
class DieListViewModel(var dice: MutableList<Die> = mutableListOf(),
                       var scoringSystems: MutableList<ScoringSystem> = mutableListOf(
                               ScoringSystem(3, "LOW"),
                               ScoringSystem(4),
                               ScoringSystem(5),
                               ScoringSystem(6),
                               ScoringSystem(7),
                               ScoringSystem(8),
                               ScoringSystem(9),
                               ScoringSystem(10),
                               ScoringSystem(11),
                               ScoringSystem(12)),
                       var currentScoringSystem: ScoringSystem = scoringSystems[0],
                       var score: MutableMap<ScoringSystem, Int> = mutableMapOf(),
                       var round: Int = 1,
                       var throws: Int = 0,
                       var isFinished: Boolean = false)
    : ViewModel(), Parcelable {

    fun restore(model: DieListViewModel) {
        this.dice = model.dice
        this.scoringSystems = model.scoringSystems
        this.currentScoringSystem = model.currentScoringSystem
        this.score = model.score
        this.round = model.round
        this.throws = model.throws
        this.isFinished = model.isFinished
    }

    /** Returns if the mutable list of die is empty. */
    fun isEmpty(): Boolean = dice.size == 0



}