package io.vrap.rmf.codegen

inline fun <R> executeAndMeasureTimeMillis(block: () -> R): Pair<R, Long> {
    val start = System.currentTimeMillis()
    val result = block()
    return result to (System.currentTimeMillis() - start)
}

fun Long.toSeconds(digits: Int) = "%.${digits}f".format(this.toDouble() / 1000.0)
