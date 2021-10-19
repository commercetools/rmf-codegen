package io.vrap.rmf.codegen

import java.util.*

inline fun <R> executeAndMeasureTimeMillis(block: () -> R): Pair<R, Long> {
    val start = System.currentTimeMillis()
    val result = block()
    return result to (System.currentTimeMillis() - start)
}

fun Long.toSeconds(digits: Int) = "%.${digits}f".format(this.toDouble() / 1000.0)

fun String.firstUpperCase(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.firstLowerCase(): String {
    return this.replaceFirstChar {
        it.lowercase(
            Locale.getDefault()
        )
    }
}
