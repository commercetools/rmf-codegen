package io.vrap.rmf.codegen.cli


import io.methvin.watcher.hashing.FileHasher
import io.methvin.watchservice.MacOSXListeningWatchService
import java.nio.file.FileSystems
import java.nio.file.WatchService
import java.util.*

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
    WARN(2),
    ERROR(3);
}

fun osDefaultWatchService(): WatchService {
    val isMac = System.getProperty("os.name").lowercase(Locale.getDefault()).contains("mac") && System.getProperty("os.arch").lowercase(Locale.getDefault()).contains("aarch64").not()
    return if (isMac) {
        MacOSXListeningWatchService(
            object : MacOSXListeningWatchService.Config {
                override fun fileHasher(): FileHasher? {
                    /**
                     * Always return null here. When MacOSXListeningWatchService is used with
                     * DirectoryWatcher, then the hashing should happen within DirectoryWatcher. If
                     * users wish to override this then they must instantiate
                     * MacOSXListeningWatchService and pass it to DirectoryWatcher.
                     */
                    return null
                }
            })
    } else {
        FileSystems.getDefault().newWatchService()
    }
}

object InternalLogger {

    var logLevel = LogLevel.INFO

    fun debug(message: String) {
        if (logLevel.level <= LogLevel.DEBUG.level) {
            println("✅   $message")
        }
    }

    fun info(message: String) {
        if (logLevel.level <= LogLevel.INFO.level) {
            println("✅   $message")
        }
    }

    fun warn(message: Throwable) {
        if (logLevel.level <= LogLevel.WARN.level) {
            println("⚠️   $message")
        }
    }

    fun error(message: String) {
        if (logLevel.level <= LogLevel.ERROR.level) {
            println("🛑   $message")
        }
    }

    fun error(throwable: Throwable) {
        if (logLevel.level <= LogLevel.ERROR.level) {
            println("🛑   $throwable")

        }
    }
}





