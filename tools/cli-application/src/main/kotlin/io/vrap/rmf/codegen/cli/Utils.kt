package io.vrap.rmf.codegen.cli

import io.vrap.rmf.raml.model.RamlDiagnostic
import io.vrap.rmf.raml.model.RamlModelResult
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.validation.Violation
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.common.util.URI
import java.nio.file.Paths
import kotlin.io.path.toPath

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

enum class OutputFormat {
    CLI,
    MARKDOWN,
    JSON;

    companion object {
        const val VALID_VALUES = "CLI, JSON, MARKDOWN"
    }
}

enum class LinkFormat {
    CLI,
    GITHUB;

    companion object {
        const val VALID_VALUES = "CLI, GITHUB"
    }
}

enum class LogLevel constructor(val level: Int) {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3);
}

enum class Printer(val printer: String) {
    LOGGER("logger"),
    GITHUB("github");

    companion object {
        fun diagnosticPrinter(printer: Printer): RamlDiagnosticPrinter {
            return when (printer) {
                GITHUB -> GithubRamlDiagnosticPrinter()
                else -> DefaultRamlDiagnosticPrinter()
            }
        }
    }
}

fun RamlDiagnostic.detailMessage(): String {
    return this.data.filterIsInstance<Violation>().find { true }?.detailMessage ?: this.message
}

interface RamlDiagnosticPrinter {
    fun print(fileURI: URI, result: RamlModelResult<Api>): Int
}

class DefaultRamlDiagnosticPrinter: RamlDiagnosticPrinter {
    override fun print(fileURI: URI, result: RamlModelResult<Api>): Int {
        val validationResults = result.validationResults

        if (validationResults.isNotEmpty()) {
            val errors = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.ERROR }
            val warnings = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.WARNING }
            val infos = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.INFO }

            if (errors.isNotEmpty()) InternalLogger.error("${errors.size} Error(s) found validating ${fileURI.toFileString()}:\n${errors.joinToString("\n") { it.toString() }}")
            if (warnings.isNotEmpty()) InternalLogger.error("${warnings.size} Warnings(s) found validating ${fileURI.toFileString()}:\n${warnings.joinToString("\n") { it.toString() }}")
            if (infos.isNotEmpty()) InternalLogger.info("${infos.size} Info(s) found validating ${fileURI.toFileString()}:\n${infos.joinToString("\n") { it.toString() }}")
            return errors.size
        }
        InternalLogger.info("Specification at ${fileURI.toFileString()} is valid.")
        return 0
    }
}

class GithubRamlDiagnosticPrinter: RamlDiagnosticPrinter {
    override fun print(fileURI: URI, result: RamlModelResult<Api>): Int {
        val validationResults = result.validationResults

        if (validationResults.isNotEmpty()) {
            val errors = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.ERROR }
            val warnings = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.WARNING }
            val infos = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.INFO }

            if (errors.isNotEmpty()) {
                InternalLogger.error("${errors.size} Error(s) found validating ${fileURI.toFileString()}")
                println(errors.joinToString("\n") { create(it) })
            }
            if (warnings.isNotEmpty()) {
                InternalLogger.error("${warnings.size} Warnings(s) found validating ${fileURI.toFileString()}")
                println(warnings.joinToString("\n") { create(it) })
            }
            if (infos.isNotEmpty()) {
                InternalLogger.info("${infos.size} Info(s) found validating ${fileURI.toFileString()}")
                println(infos.joinToString("\n") { create(it) })
            }
            return errors.size
        }
        InternalLogger.info("Specification at ${fileURI.toFileString()} is valid.")
        return 0
    }

    fun create(diagnostic: RamlDiagnostic): String {
        val path = Paths.get("").toAbsolutePath()
        val t = path.relativize(java.net.URI.create(diagnostic.location).toPath())
        val properties = String.format(
            "file=%s,line=%d,col=%d,title=%s",
            t,
            diagnostic.line,
            diagnostic.column,
            escapeProperty(diagnostic.message)
        )

        return String.format(
            "::%s %s\n",
            when (diagnostic.severity) {
                Diagnostic.ERROR -> "error"
                Diagnostic.WARNING -> "warning"
                else -> "info"
            },
            properties
        )
    }

    private fun escapeProperty(value: String): String
    {
        val map = mapOf("%" to "%25", "\n" to "%0D", "\n" to "%0A", ":" to "%3A", "," to "%2C")
        return value.map { map.getOrDefault(it.toString(), it) }.joinToString("")
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





