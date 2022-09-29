package io.vrap.rmf.codegen.cli

import com.fasterxml.jackson.databind.ObjectMapper
import io.vrap.rmf.codegen.cli.diff.*
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.modules.Api
import org.eclipse.emf.common.util.URI
import picocli.CommandLine
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Callable
import java.util.stream.Collectors

enum class OutputFormat {
    CLI,
    JSON,
}
const val ValidFormats =  "CLI, JSON"

@CommandLine.Command(name = "diff", description = ["Generates a diff between two specifications"])
class DiffSubcommand : Callable<Int> {

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    @CommandLine.Parameters(index = "0", description = ["Original api file location"])
    lateinit var originalFileLocation: Path

    @CommandLine.Parameters(index = "1", description = ["Changed api file location"])
    lateinit var changedFileLocation: Path

    @CommandLine.Option(names = ["-f", "--format"], description = ["Specifies the output format","Valid values: $ValidFormats"])
    var outputFormat: OutputFormat = OutputFormat.CLI

    @CommandLine.Option(names = ["-t", "--target"], description = ["Specifies the file to write to"])
    var outputTarget: Path? = null

    @CommandLine.Option(names = ["-d", "--diffs"], description = ["Diff configuration"], required = false)
    var diffConfigurationFile: Path? = null

    override fun call(): Int {
        return diff()
    }

    private fun diff(): Int {
        val originalApi = readApi(originalFileLocation.toRealPath().toAbsolutePath()) ?: return 1
        val changedApi = readApi(changedFileLocation.toRealPath().toAbsolutePath()) ?: return 1

        val config = diffConfigurationFile?.toFile()?.inputStream() ?: ValidateSubcommand::class.java.getResourceAsStream("/diff.xml")

        val differ = RamlDiff.Builder()
            .original(originalApi)
            .changed(changedApi)
            .plus(DiffSetup.setup(config))
            .build()
        val diffResult = differ.diff()

        if (diffResult.isEmpty()) {
            return 0
        }

        val output = when(outputFormat) {
            OutputFormat.CLI -> CliFormatPrinter().print(diffResult)
            OutputFormat.JSON -> JsonFormatPrinter().print(diffResult)
        }

        outputTarget?.let {
            Files.write(it, output.toByteArray(StandardCharsets.UTF_8))
        } ?: run {
            println(output)
        }
        return 0
    }

    class CliFormatPrinter {
        fun print(diffResult: List<Diff<Any>>): String {
            return diffResult.joinToString("\n") { "${it.message} (${it.source})" }
        }
    }

    class JsonFormatPrinter {
        fun print(diffResult: List<Diff<Any>>): String {
            val mapper = ObjectMapper()
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(diffResult)
        }
    }

    private fun readApi(fileLocation: Path): Api? {
        val fileURI = URI.createURI(fileLocation.toUri().toString())
        InternalLogger.info("Reading ${fileURI.toFileString()} ...")
        val modelResult = RamlModelBuilder().buildApi(fileURI)
        val validationResults = modelResult.validationResults
        if (validationResults.isNotEmpty()) {
            val res = validationResults.stream().map { "$it" }.collect( Collectors.joining( "\n" ) );
            InternalLogger.error("Error(s) found validating ${fileURI.toFileString()}:\n$res")
            return null
        }
        InternalLogger.info("\tdone")

        return modelResult.rootObject
    }
}
