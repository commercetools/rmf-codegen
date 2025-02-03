package io.vrap.rmf.codegen.cli

import com.commercetools.rmf.diff.*
import com.fasterxml.jackson.databind.ObjectMapper
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.java.base.extensions.resourcePathList
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import org.eclipse.emf.common.util.URI
import picocli.CommandLine
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Callable
import java.util.stream.Collectors

@CommandLine.Command(name = "diff", description = ["Generates a diff between two specifications"])
class DiffSubcommand : Callable<Int> {

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    @CommandLine.Parameters(index = "0", description = ["Original api file location"])
    lateinit var originalFileLocation: Path

    @CommandLine.Parameters(index = "1", description = ["Changed api file location"])
    lateinit var changedFileLocation: Path

    @CommandLine.Option(names = ["-f", "--format"], description = ["Specifies the output format","Valid values: ${OutputFormat.VALID_VALUES}"])
    var outputFormat: OutputFormat = OutputFormat.CLI

    @CommandLine.Option(names = ["-o", "--outputTarget"], description = ["Specifies the file to write to"])
    var outputTarget: Path? = null

    @CommandLine.Option(names = ["-d", "--diffs"], description = ["Diff configuration"])
    var diffConfigurationFile: Path? = null

    @CommandLine.Option(names = ["-s", "--severity"], description = ["Check severity", "Valid values: ${CheckSeverity.VALID_VALUES}"])
    var checkSeverity: CheckSeverity = CheckSeverity.FATAL

    override fun call(): Int {
        return diff()
    }

    private fun diff(): Int {
        val originalApi = readApi(originalFileLocation.toRealPath().toAbsolutePath())
        val changedApi = readApi(changedFileLocation.toRealPath().toAbsolutePath())

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

        val output = diagnosticPrinter(outputFormat).print(diffResult)

        outputTarget?.let {
            InternalLogger.info("Writing to ${it.toAbsolutePath().normalize()}")
            Files.write(it.toAbsolutePath().normalize(), output.toByteArray(StandardCharsets.UTF_8))
        } ?: run {
            println(output)
        }
        return diffResult.any { diff -> diff.severity >= checkSeverity }.let { b -> if(b) 1 else 0 }
    }

    companion object {
        fun diagnosticPrinter(printer: OutputFormat): FormatPrinter {
            return when (printer) {
                OutputFormat.CLI -> CliFormatPrinter()
                OutputFormat.MARKDOWN -> MarkdownFormatPrinter()
                OutputFormat.JAVA_MARKDOWN -> JavaMarkdownFormatPrinter()
                OutputFormat.PHP_MARKDOWN -> PHPMarkdownFormatPrinter()
                OutputFormat.TS_MARKDOWN -> TSMarkdownFormatPrinter()
                OutputFormat.DOTNET_MARKDOWN -> DotNetMarkdownFormatPrinter()
                OutputFormat.GITHUB -> CliFormatPrinter()
                OutputFormat.JSON -> JsonFormatPrinter()
            }
        }
    }

    private fun readApi(fileLocation: Path): Api {
        val fileURI = URI.createURI(fileLocation.toUri().toString())
        InternalLogger.info("Reading ${fileURI.toFileString()} ...")
        val modelResult = RamlModelBuilder().buildApi(fileURI)
        val validationResults = modelResult.validationResults
        if (validationResults.isNotEmpty()) {
            val res = validationResults.stream().map { "$it" }.collect( Collectors.joining( "\n" ) );
            InternalLogger.error("Error(s) found validating ${fileURI.toFileString()}:\n$res")
            return modelResult.rootObject
        }
        InternalLogger.info("\tdone")

        return modelResult.rootObject
    }

}
