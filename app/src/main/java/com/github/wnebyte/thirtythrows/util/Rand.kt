package com.github.wnebyte.thirtythrows.util

import java.util.*

class Rand {

    companion object {

        private val rand = Random()

        /**
         * Returns a uniformly distributed random integer between 1 and the specified upperBounds
         * (inclusive).
         */
        fun int(upperBounds: Int): Int {
            var value = rand.nextInt(upperBounds + 1)

            while (value == 0) {
                value = rand.nextInt(upperBounds + 1)
            }

            return value
        }
    }
}