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
 * Die model class.
 */
data class Die(val value: Int = Random().nextInt(1, 6),
               var throwAgain: Boolean = true,
               val id: UUID = UUID.randomUUID()
): Parcelable {

    private constructor(parcel: Parcel) : this(
            value = parcel.readInt(),
            throwAgain = Boolean.fromInt(parcel.readInt()),
            id = UUID.fromString(parcel.readString())
    )

    init {
        if (!Range(1, 6).contains(value)) {
            throw IllegalArgumentException(
                    "number must be between 1 and 6."
            )
        }
    }

    companion object {

        fun newInstance(): Die = Die()

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