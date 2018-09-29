package io.vrap.rmf.codegen.kt.rendring.utils


import java.util.*
import java.util.concurrent.atomic.AtomicInteger


fun validateString(input: String, indStartToken: Char, indStopToken: Char, escapeChar: Char) {
    val stack = Stack<Pair<Int, Int>>()

    input.lines()
            .forEachIndexed { index, s ->
                var i = 0
                while (i < s.length) {
                    when (s[i]) {
                        escapeChar -> i++
                        indStartToken -> stack.push(Pair(index, i))
                        indStopToken -> {
                            if (stack.isEmpty()) {
                                throw Exception("can't find opening token '$indStartToken' for closing token '$indStopToken' at line $index column $i \n$input")
                            } else {
                                stack.pop()
                            }
                        }
                    }
                    i++
                }
            }

    if (stack.isNotEmpty()) {
        val lastOpenedIndentation = stack.pop()
        throw Exception("can't find closing token '$indStopToken' for open token '$indStartToken' at line ${lastOpenedIndentation.first} column ${lastOpenedIndentation.second} \n$input ")
    }

}

fun indentString(input: String, result: StringBuilder = StringBuilder(),
                 initialPadding: String = "",
                 index: AtomicInteger = AtomicInteger(0),
                 heapDepth:Int = 0,
                 candidateEmptyLinesIndexes : MutableList<Pair<Int,Int>> = mutableListOf(),
                 indStartToken: Char,
                 indStopToken: Char,
                 escapeChar: Char) {

    val padding = StringBuilder()
    while (index.get() < input.length) {
        when (input[index.get()]) {
            escapeChar -> {
                val nexIndex = index.incrementAndGet()
                if (nexIndex < input.length) {
                    result.append(input[nexIndex])
                }
            }
            indStartToken -> {
                val starIndex = result.lastIndex
                index.incrementAndGet()
                indentString(input, result, initialPadding + padding, index,heapDepth+1, candidateEmptyLinesIndexes,indStartToken, indStopToken, escapeChar)
                candidateEmptyLinesIndexes.add(Pair(starIndex,result.length))

            }
            indStopToken -> return
            '\n' -> {
                padding.setLength(0)
                result.append("\n$initialPadding")
            }
            else -> {
                result.append(input[index.get()])
                padding.append(' ')
            }
        }
        index.incrementAndGet()
    }

    if(heapDepth == 0 && candidateEmptyLinesIndexes.isNotEmpty()){
        //remove empty lines
        candidateEmptyLinesIndexes.reverse()
        candidateEmptyLinesIndexes.forEach{removeIfLineIsEmpty(result,it.first,it.second)}
    }
}

fun removeIfLineIsEmpty(stringBuilder: StringBuilder, start:Int,end:Int){

    if(stringBuilder.substring(start,end).isNotBlank()){
        return
    }

    var lineStart=start
    var lineEnd =end
    while(lineStart>0 && stringBuilder[lineStart] != '\n'){
        lineStart--
    }
    while(lineEnd<stringBuilder.lastIndex && stringBuilder[lineEnd] != '\n'){
        lineEnd++
    }
    if(stringBuilder.substring(lineStart,lineEnd).isBlank() && stringBuilder[lineStart] == '\n'){
        stringBuilder.replace(lineStart,lineEnd,"")
    }
}



fun generateTemplate(input: String, indStartToken: Char = '<', indStopToken: Char = '>', escapeChar: Char = '\\'): String {


    if (indStartToken == indStopToken) throw Exception("the indentation start and stop token should be different")

    // First you need to validate the string
    validateString(input, indStartToken, indStopToken, escapeChar)

    //To protect from platform dependent line return (not sure if necessary though :) )
    val changedInput = input.replace(System.lineSeparator(), "\n")

    //Now you can indent properly
    val result = StringBuilder()
    indentString(changedInput, result, indStartToken = indStartToken, indStopToken = indStopToken, escapeChar = escapeChar)

    return result.toString()
}

fun String.keepIndentation() = generateTemplate(this)

/**
 * Escape all special characters such as '<' '>' '\'
 */
fun String.escapeAll(escapeChar: Char = '\\') = this.replace("$escapeChar", "$escapeChar$escapeChar")
        .replace("<", "$escapeChar<")
        .replace(">", "$escapeChar>")
