package com.github.wnebyte.thirtythrows.model

import android.os.Parcel
import android.os.Parcelable
import com.google.common.collect.Sets

/**
 * This class represents the score and related scoring-system obtained after completing
 * a round of ThirtyThrows.
 */
data class Round(
        val system: Int,
        val score: Int
): Parcelable {

        /**
         * Constructs a new instance using the specified [parcel].
         * @parcel to be used.
         */
        private constructor(parcel: Parcel) : this(
                system = parcel.readInt(),
                score = parcel.readInt()
        )

        companion object {

                /**
                 * Constructs a new instance after calculating the score from the specified
                 * [dice] and [system], and assigns the calculated score alongside the
                 * `system` to the new instance.
                 * @param dice to be used to calculate the score.
                 * @param system the used to calculate the score.
                 * @return a new `round` instance.
                 */
                fun newInstance(dice: Set<Die>, system: Int): Round =
                        Round(system, score(dice, system))

                /**
                 * Calculates and returns a score using the specified [dice] and [system].
                 * @param dice to be used to calculate a score.
                 * @param system to be used to calculate a score.
                 * @return a score.
                 */
                private fun score(dice: Set<Die>, system: Int): Int =
                        when (system) {
                                3 -> low(dice)
                                else -> high(dice, system)
                        }

                /**
                 * Calculates and returns a low score using the specified [dice].
                 * @param dice to be used to calculate a score.
                 * @return a score.
                 */
                private fun low(dice: Set<Die>): Int {
                        var score = 0

                        for (die in dice) {
                                if (die.value <= 3) {
                                        score += die.value
                                }
                        }

                        return score
                }

                /**
                 * Calculates and returns a high score using the specified [dice] and [system].
                 * @param dice to be used to calculate a score.
                 * @param system to be used to calculate a score.
                 * @return a score.
                 */
                private fun high(dice: Set<Die>, system: Int): Int {
                        val ps: Set<Set<Die>> = Sets.powerSet(dice)
                        var max = 0

                        for (set in ps) {
                                val sum: Int = set.map { d -> d.value }.sum()
                                if ((sum % system == 0) && (max < sum)) {
                                        max = sum
                                }
                        }

                        return max
                }

                @JvmField
                val CREATOR = object : Parcelable.Creator<Round> {

                        override fun createFromParcel(parcel: Parcel): Round = Round(parcel)

                        override fun newArray(size: Int): Array<Round?> = arrayOfNulls(size)
                }
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(system)
                parcel.writeInt(score)
        }

        override fun describeContents(): Int = 0
}