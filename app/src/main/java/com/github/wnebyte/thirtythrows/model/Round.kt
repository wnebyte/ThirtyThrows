package com.github.wnebyte.thirtythrows.model

import android.os.Parcel
import android.os.Parcelable
import com.google.common.collect.Sets

data class Round(
        val system: Int,
        val score: Int
): Parcelable {

        private constructor(parcel: Parcel) : this(
                system = parcel.readInt(),
                score = parcel.readInt()
        )

        companion object {

                fun newInstance(dice: Set<Die>, system: Int): Round =
                        Round(system, score(dice, system))

                private fun score(dice: Set<Die>, num: Int): Int =
                        when (num) {
                                3 -> low(dice)
                                else -> high(dice, num)
                        }

                private fun low(dice: Set<Die>): Int {
                        var score = 0

                        for (die in dice) {
                                if (die.value <= 3) {
                                        score += die.value
                                }
                        }

                        return score
                }

                private fun high(dice: Set<Die>, num: Int): Int {
                        val ps: Set<Set<Die>> = Sets.powerSet(dice)
                        var max = 0

                        for (set in ps) {
                                val sum: Int = set.map { d -> d.value }.sum()
                                if ((sum % num == 0) && (max < sum)) {
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