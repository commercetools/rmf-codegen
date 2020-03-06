package io.vrap.rmf.codegen.cli

import java.io.PrintWriter

/**
 * This method tries to run a block, if failed it will log the exception and not throw it
 */
fun safeRun(block: () -> Int): Int {
    return try {
        return block()
    } catch (e: Throwable) {
        InternalLogger.error(e)
        1
    }
}


enum class LogLevel constructor(val level: Int) {
    DEBUG(0),
    INFO(1),
    ERROR(2);
}


object InternalLogger {
    val messageWriter = PrintWriter(System.out)
    val errorWriter = PrintWriter(System.err)

    var logLevel = LogLevel.INFO

    fun debug(message: String) {
        if (logLevel.level <= LogLevel.DEBUG.level) {
            messageWriter.print("[DEBUG] ")
            messageWriter.println(message)
            messageWriter.flush()
        }
    }

    fun info(message: String) {
        if (logLevel.level <= LogLevel.INFO.level) {
            messageWriter.print("✅   ")
            messageWriter.println(message)
            messageWriter.flush()
        }
    }

    fun error(message: String) {
        if (logLevel.level <= LogLevel.ERROR.level) {
            errorWriter.print("\uD83D\uDED1    ")
            errorWriter.println(message)
            errorWriter.flush()
        }
    }

    fun error(throwable: Throwable) {
        if (logLevel.level <= LogLevel.ERROR.level) {
            errorWriter.print("\uD83D\uDED1    ")
            errorWriter.println(throwable)
            errorWriter.flush()
        }
    }
}





