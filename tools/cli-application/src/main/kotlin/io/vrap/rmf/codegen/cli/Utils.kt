package io.vrap.rmf.codegen.cli

import java.io.PrintWriter

/**
 * This method tries to run a block, if failed it will log the exception and not throw it
 */
fun safeRun(block: () -> Int): Int{
    return try{
        return block()
    } catch (e:Exception){
        printError(e.toString())
        1
    }
}

val messageWriter = PrintWriter(System.out)
val errorWriter = PrintWriter(System.err)

fun printMessage(message:String){
    messageWriter.print("✅   ")
    messageWriter.println(message)
    messageWriter.flush()
}

fun printError(message:String){
    errorWriter.print("\uD83D\uDED1    ")
    errorWriter.println(message)
    errorWriter.flush()
}