package io.vrap.rmf.codegen.kt.rendring.utils


import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * This method allows you to validate a string that for each open indStartToken char there is a corresponding indStopToken, if its not the case
 * this method throws an exception showing the line number of the non closed token
 */
fun validateString(input: String, indStartToken: String, indStopToken: String, escapeChar: Char) {
    val stack = Stack<Pair<Int, Int>>()

    input.lines()
            .forEachIndexed { index, s ->
                var i = 0
                while (i < s.length) {
                    when (s[i]) {
                        escapeChar -> i++
                        indStartToken[0] ->
                            when (s.substring(i, Math.min(s.length, i + indStartToken.length))) {
                                indStartToken -> stack.push(Pair(index, i))
                            }
                        indStopToken[0] -> {
                            when (s.substring(i, Math.min(s.length, i + indStopToken.length))) {
                                indStopToken ->
                                    if (stack.isEmpty()) {
                                        val numerizedLinez = input.lines().mapIndexed { index, s -> "$index - $s" }.joinToString(separator = "\n")
                                        throw Exception("can't find opening token '$indStartToken' for closing token '$indStopToken' at line $index column $i \n$numerizedLinez")
                                    } else {
                                        stack.pop()
                                    }
                            }
                        }
                    }
                    i++
                }
            }

    if (stack.isNotEmpty()) {
        val lastOpenedIndentation = stack.pop()
        val numerizedLinez = input.lines().mapIndexed { index, s -> "$index - $s" }.joinToString(separator = "\n")
        throw Exception("can't find closing token '$indStopToken' for open token '$indStartToken' at line ${lastOpenedIndentation.first} column ${lastOpenedIndentation.second} \n$numerizedLinez ")
    }
}


/**
 * This method allow you to indent the input string accordingly, meaning once countering an indStartToken all following lines are indented by the same amount of indents as the first line
 *
 *
 * @param input The sttring to be indented
 * @param result a StringBuilder to accumulate the result string
 * @param initialPadding The initial padding that will be applied to all the lines in the string
 * @param index The index of the input string processed so far
 * @param heapDepth shows how deep in the recursion heap now
 * @param candidateEmptyLinesIndexes this keep the line index where the indentation is applied, this is good because this empty lines might be empty and then should be removed later
 * @param indStartToken the token used to mark a new indented substring
 * @param indStopToken the token used to mark the end of the new indented substring
 * @param escapeChar if the indStartToken would be escaped, it should be proceeded by this escape character
 */
fun indentString(input: String,
                 result: StringBuilder = StringBuilder(),
                 initialPadding: String = "",
                 index: AtomicInteger = AtomicInteger(0),
                 heapDepth:Int = 0,
                 candidateEmptyLinesIndexes : MutableList<Pair<Int,Int>> = mutableListOf(),
                 indStartToken: String,
                 indStopToken: String,
                 escapeChar: Char) : StringBuilder{

    val padding = StringBuilder()
    while (index.get() < input.length) {
        when (input[index.get()]) {
            escapeChar -> {
                val nexIndex = index.incrementAndGet()
                if (nexIndex < input.length) {
                    result.append(input[nexIndex])
                }
            }
            indStartToken[0] -> {
                when (input.substring(index.get(), Math.min(input.length, index.get() + indStartToken.length))) {
                    indStartToken -> {
                        val starIndex = result.lastIndex
                        index.incrementAndGet()
                        indentString(input, result, initialPadding + padding, index,heapDepth+1, candidateEmptyLinesIndexes,indStartToken, indStopToken, escapeChar)
                        candidateEmptyLinesIndexes.add(Pair(starIndex,result.length))
                    }
                }
            }
            indStopToken[0] -> {
                when (input.substring(index.get(), Math.min(input.length, index.get() + indStopToken.length))) {
                    indStopToken -> return result
                }
            }
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
    return result
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



fun generateTemplate(input: String, indStartToken: String = "<", indStopToken: String = ">", escapeChar: Char = '\\'): String {


    if (indStartToken[0] == indStopToken[0] || indStartToken == indStopToken) throw Exception("the indentation start and stop token should be different")

    // First you need to validate the string
    validateString(input, indStartToken, indStopToken, escapeChar)

    //To protect from platform dependent line return (not sure if necessary though :) )
    val changedInput = input.replace(System.lineSeparator(), "\n")

    //Now you can indent properly
    val result = indentString(changedInput, indStartToken = indStartToken, indStopToken = indStopToken, escapeChar = escapeChar)

    return result.toString()
}

/**
 * This method allow you to keep the correct indentation of your template,lets take the following example
 *
 * `
 * """
 * |Here are the benefits of eating one apple a day: <${benefits(FruitType.APPLE)}>
 * |
 * """`
 *
 * if we process this string with `keepIndentation`, and ${benefits(FruitType.APPLE) return
 * a multiline string then each new line would be indented to start from same index where the
 * first line started which would result of something like this after evaluation
 *`
 * """
 * |Here are the benefits of eating one apple a day: - keeps the doctor away
 * |                                                 - it's good
 * |                                                 - just eat it already.
 * """
 *`
 * this is particularly useful for code generation since the new block included in the template would keep the indentation from where it was specified,
 * this give the code a nice formatting and for some languages such as `python`, this is not only a convenience but a necessity since the indentation is used there
 * to identify code blocks
 *
 * if your template contains `'<'` or `'>'`, `'\'` that weren't meant for indentation semantics, then they would have to be skipped via the skipping char '\'
 * (this is the default skipping character but can be changed),in that they would be ignored by the parser or you can escape all the special chars you can
 * use `escapeAll` which would escape every special character in that string.
 */
fun String.keepIndentation() = generateTemplate(this)
fun String.keepIndentation(indStartToken: String, indStopToken: String) = generateTemplate(this, indStartToken, indStopToken)

/**
 * Escape all special characters such as '<' '>' '\'
 */
fun String.escapeAll(escapeChar: Char = '\\') = this.replace("$escapeChar", "$escapeChar$escapeChar")
        .replace("<", "$escapeChar<")
        .replace(">", "$escapeChar>")
