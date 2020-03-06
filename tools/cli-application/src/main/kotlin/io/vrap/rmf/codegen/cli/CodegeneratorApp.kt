@file:JvmName("MainKt")

package io.vrap.rmf.codegen.cli

import io.vrap.rmf.codegen.cli.info.BuildInfo
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.util.concurrent.Callable
import kotlin.system.exitProcess



fun main(args: Array<String>) {

    val exitCode = CommandLine(RMFCommand())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .setOut(messageWriter)
            .setErr(errorWriter)
            .setColorScheme(CommandLine.Help.defaultColorScheme(CommandLine.Help.Ansi.ON))
            .execute(*args)

    exitProcess(exitCode)
}

@Command(
        version = [BuildInfo.VERSION],
        description = ["Allows to validate Raml files and generate code from them"],
        subcommands = [GenerateSubcommand::class, VerifySubcommand::class]
)
class RMFCommand : Callable<Int> {

    @Option(names = ["-v", "--version"], versionHelp = true, description = ["print version information and exit"])
    var versionRequested = false

    @Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    override fun call(): Int {
        printError("Please invoke a subcommand");
        CommandLine(this).usage(errorWriter);
        return 0
    }
}
