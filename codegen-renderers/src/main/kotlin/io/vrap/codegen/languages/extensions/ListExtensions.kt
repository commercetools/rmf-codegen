package io.vrap.codegen.languages.extensions

private class TopologySortContext<T>(val predecessors: Map<T, List<T>>) {

    fun hasNoPredecessors(node: T) : Boolean {
        return predecessors[node]?.size == 0
    }

    fun remove(node : T) : TopologySortContext<T> {
        return TopologySortContext(predecessors.mapValues { it.value.minus(node) })
    }
}

/**
 * Sorts the list by topology defined by the given predecessor function.
 * https://en.wikipedia.org/wiki/Topological_sorting
 *
 * @param predecessorsOf function that returns the predecessors of the given argument
 * @throws IllegalArgumentException thrown when the list contains cycles
 * @return the topologically sorted list
 */
fun <T> List<T>.sortedByTopology(predecessorsOf: (T) -> List<T>) : List<T> {
    val nodes = ArrayList(this)
    val sorted = ArrayList<T>()
    val predecessors = nodes.associateWith(predecessorsOf).mapValues { it.value.filter { nodes.contains(it) } }
    var context = TopologySortContext(predecessors)
    var changes = true
    outer@ while(changes) {
        changes = false
        for (node in nodes) {
            if (context.hasNoPredecessors(node)) {
                sorted.add(node)
                nodes.remove(node)
                context = context.remove(node)
                changes = true
                continue@outer
            }
        }
    }
    if (nodes.size > 0)
        throw IllegalArgumentException("list contains cycles")

    return sorted
}
