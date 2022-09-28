package io.vrap.rmf.codegen.cli

import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.modules.Api
import org.eclipse.emf.common.util.URI
import picocli.CommandLine
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

    override fun call(): Int {
        return diff()
    }

    private fun diff(): Int {
        val originalApi = readApi(originalFileLocation.toRealPath().toAbsolutePath()) ?: return 1
        val changedApi = readApi(changedFileLocation.toRealPath().toAbsolutePath()) ?: return 1

        val diffResult = ApiDiffer().diff(originalApi, changedApi)

        if (diffResult.isEmpty()) {
            return 0
        }
        println(diffResult.joinToString("\n") { "${it.message} (${it.source})" })
        return 1
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
