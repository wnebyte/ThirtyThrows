package com.github.wnebyte.thirtythrows.ext

class ArrayListExt {

    companion object {

        /**
         * Adds the specified [elements] to this ArrayList.
         * @param elements to be added.
         * @param E type of the specified elements.
         */
        fun <E> ArrayList<E>.addAll(vararg elements: E) {
            if (elements.isEmpty()) {
                return
            }
            for (e in elements) {
                this.add(e)
            }
        }
    }
}