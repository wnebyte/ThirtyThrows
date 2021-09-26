package com.github.wnebyte.thirtythrows.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Range
import java.lang.IllegalArgumentException
import java.util.*
import com.github.wnebyte.thirtythrows.ext.BooleanExt.Companion.fromInt
import com.github.wnebyte.thirtythrows.ext.BooleanExt.Companion.toInt
import com.github.wnebyte.thirtythrows.ext.RandomExt.Companion.nextInt

/**
 * This class represents a parcelable, six-sided die that can find it self being in one of two
 * mutable states.
 */
/*
this class has an id field so that two die of the same value/state do not equal one another.
 */
data class Die(val value: Int = Random().nextInt(1, 6),
               var throwAgain: Boolean = true,
               val id: UUID = UUID.randomUUID()
): Parcelable {

    /**
     * Constructs a new instance using the specified [parcel].
     * @param parcel to be used.
     */
    private constructor(parcel: Parcel) : this(
            value = parcel.readInt(),
            throwAgain = Boolean.fromInt(parcel.readInt()),
            id = UUID.fromString(parcel.readString())
    )

    /*
    does not allow the construction of a die with a value outside the inclusive range (1,6).
     */
    init {
        if (!Range(1, 6).contains(value)) {
            throw IllegalArgumentException(
                    "number must be between 1 and 6."
            )
        }
    }

    companion object {

        /**
         * Constructs a new instance with default parameters.
         * @return a new `die` instance.
         */
        fun newInstance(): Die = Die()

        /**
         * Constructs a new instance using the specified [value].
         * @param value to be assigned to the new instance.
         * @return a new `die` instance.
         */
        fun newInstance(value: Int): Die = Die(value)

        @JvmField
        val CREATOR = object : Parcelable.Creator<Die> {

            override fun createFromParcel(parcel: Parcel): Die = Die(parcel)

            override fun newArray(size: Int): Array<Die?> = arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(value)
        parcel.writeInt(throwAgain.toInt())
        parcel.writeString(id.toString())
    }

    override fun describeContents(): Int = 0
}