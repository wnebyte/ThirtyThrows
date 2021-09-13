package com.github.wnebyte.thirtythrows.model

import android.os.Parcel
import android.os.Parcelable
import com.github.wnebyte.thirtythrows.util.Rand
import java.lang.IllegalArgumentException

/**
 * Die model class.
 */
data class Die(val number: Int = Rand.int(6),
               var throwAgain: Boolean = true,
               var group: Int = 0) : Parcelable {
    init {
        if (number < 1 || 6 < number) {
            throw IllegalArgumentException("number must be between 1 and 6.")
        }

        if (group < 0 || 6 < group) {
            throw IllegalArgumentException("group must be between 0 and 6.")
        }
    }

    fun toggleGroup() {
        if (group == 6) {
            group = 0
        }
        else {
            group++
        }
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Die> {

            override fun createFromParcel(parcel: Parcel): Die = Die(parcel)

            override fun newArray(size: Int): Array<Die?> = arrayOfNulls<Die>(size)
        }

        private fun toInt(bool: Boolean): Int {
            return if (bool) {
                1
            } else {
                0
            }
        }

        private fun toBoolean(int: Int): Boolean {
            return int != 0
        }
    }

    private constructor(parcel: Parcel) : this(
            number = parcel.readInt(),
            throwAgain = toBoolean(parcel.readInt()),
            group = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
        parcel.writeInt(toInt(throwAgain))
        parcel.writeInt(group)
    }

    override fun describeContents(): Int = 0

}