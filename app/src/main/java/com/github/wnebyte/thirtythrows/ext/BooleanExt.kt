package com.github.wnebyte.thirtythrows.ext

/**
 * This class declares extension functions for the [Boolean] class.
 */
class BooleanExt {

    companion object {

        /**
         * Calls [Boolean.Companion.toInt].
         */
        fun Boolean.toInt(): Int {
            return Boolean.toInt(this)
        }

        /**
         * Converts the specified [value] into an Int.
         * @param value to be converted.
         * @return 1 if the specified value was true,
         * or 0 if the specified value was false.
         */
        fun Boolean.Companion.toInt(value: Boolean): Int {
            return when (value) {
                true -> 1
                false -> 0
            }
        }

        /**
         * Converts the specified [value] into a Boolean.
         * @param value to be converted.
         * @return true if the specified value was 1,
         * or false if the specified value was 0.
         * @throws IllegalArgumentException if the specified value was neither 1 nor 0.
         */
        fun Boolean.Companion.fromInt(value: Int): Boolean {
            return when (value) {
                1 -> true
                0 -> false
                else -> throw IllegalArgumentException(
                        ""
                )
            }
        }
    }
}