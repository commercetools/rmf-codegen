@file:JvmName("MainKt")

package io.vrap.rmf.codegen.cli

import ch.qos.logback.classic.Level
import io.vrap.rmf.codegen.cli.info.BuildInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.AutoComplete
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.util.concurrent.Callable
import kotlin.system.exitProcess



fun main(args: Array<String>) {

    val exitCode = CommandLine(RMFCommand())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(*args)

    exitProcess(exitCode)
}

@Command(
        version = [BuildInfo.VERSION],
        description = ["Allows to validate Raml files and generate code from them"],
        subcommands = [GenerateSubcommand::class, VerifySubcommand::class, ValidateSubcommand::class, DiffSubcommand::class, AutoComplete.GenerateCompletion::class],
)
class RMFCommand : Callable<Int> {

    @Option(names = ["-v", "--version"], versionHelp = true, description = ["print version information and exit"])
    var versionRequested = false

    @Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    @Option(names = ["--loglevel"], description = ["log level"], required = false)
    var logLevel: LogLevel = LogLevel.INFO

    override fun call(): Int {
        InternalLogger.error("Please invoke a subcommand");
        CommandLine(this).usage(System.out);
        return 0
    }

    fun loglevel(): Level {
        return when(logLevel) {
            LogLevel.INFO -> Level.INFO
            LogLevel.WARN -> Level.WARN
            LogLevel.ERROR -> Level.ERROR
            LogLevel.DEBUG -> Level.DEBUG
        }
    }
}
