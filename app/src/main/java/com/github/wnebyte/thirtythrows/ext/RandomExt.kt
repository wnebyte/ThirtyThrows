package com.github.wnebyte.thirtythrows.ext

import java.lang.IllegalArgumentException
import java.util.Random

/**
 * This class declares extension functions for the [Random] class.
 */
class RandomExt {

    companion object {

        /**
         * Returns a pseudorandom uniformly distributed `int` value between
         * the specified [lower] (inclusive) and the specified [upper] (inclusive).
         * @param lower the lower bound (inclusive). Must be smaller than upper.
         * @param upper the upper bound (inclusive). Must be positive.
         * @return the next pseudorandom `int` value between the specified bounds.
         */
        fun Random.nextInt(lower: Int, upper: Int): Int {
            if (upper <= lower) {
                throw IllegalArgumentException(
                        "lower must be smaller than upper"
                )
            }
            var value = lower - 1
            while (value < lower) {
                value = this.nextInt(upper + 1)
            }
            return value
        }
    }
}