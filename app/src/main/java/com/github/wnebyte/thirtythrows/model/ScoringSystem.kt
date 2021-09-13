package com.github.wnebyte.thirtythrows.model

import android.os.Parcel
import android.os.Parcelable
import java.lang.IllegalArgumentException

/**
 * ScoringSystem model class.
 */
data class ScoringSystem(val number: Int,
                         val text: String = if (number == 3) "LOW" else number.toString())
    : Parcelable {
    init {
        if (number < 3 || 12 < number) {
            throw IllegalArgumentException("number must be between 3 and 12.")
        }
    }

    override fun toString(): String = this.text

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ScoringSystem> {

            override fun createFromParcel(parcel: Parcel): ScoringSystem = ScoringSystem(parcel)

            override fun newArray(size: Int): Array<ScoringSystem?> = arrayOfNulls(size)
        }
    }

    private constructor(parcel: Parcel) : this(
            number = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
    }

    override fun describeContents(): Int = 0
}