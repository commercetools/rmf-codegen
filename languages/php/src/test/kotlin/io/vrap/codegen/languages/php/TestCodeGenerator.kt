package io.vrap.codegen.languages.php

import io.vrap.codegen.languages.php.base.PhpBaseModule
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.codegen.languages.php.test.PhpTestModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Assertions

import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths

class TestCodeGenerator {

    companion object {
        private val runPhpUnitTest = System.getenv("RUN_PHPUNIT") == "true"
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val importerUserProvidedPath = System.getenv("IMPORTER_RAML_FILE")
        private val mlUserProvidedPath = System.getenv("ML_RAML_FILE")
        private val userProvidedOutputPath = System.getenv("OUTPUT_FOLDER")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        private val importerPath : Path = Paths.get(if (importerUserProvidedPath == null) "../../api-spec/api.raml" else importerUserProvidedPath)
        private val mlPath : Path = Paths.get(if (mlUserProvidedPath == null) "../../api-spec/api.raml" else mlUserProvidedPath)
        private val outputFolder : Path = Paths.get(if (userProvidedOutputPath == null) "build/gensrc" else userProvidedOutputPath)
        val apiProvider: RamlApiProvider = RamlApiProvider(apiPath)
        val importProvider: RamlApiProvider = RamlApiProvider(importerPath)
        val mlProvider: RamlApiProvider = RamlApiProvider(mlPath)
    }

    @Test
    fun generateApiSdk() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools/api",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-api")
        )

        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, PhpModelModule)
        generatorComponent.generateFiles()

        if (runPhpUnitTest && userProvidedPath != null) {
            runPHPUnitTests(outputFolder.parent, "base,api")
        }

        val generatorConfigTests = CodeGeneratorConfig(
                basePackageName = "commercetools/api",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-api-tests")
        )

        val generatorModuleTests = RamlGeneratorModule(apiProvider, generatorConfigTests, PhpBaseTypes)
        val generatorComponentTests = RamlGeneratorComponent(generatorModuleTests, PhpTestModule)
        generatorComponentTests.generateFiles()
    }

    @Test
    fun generateImportSdk() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools/import",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-import")
        )

        val generatorModule = RamlGeneratorModule(importProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, PhpModelModule)
        generatorComponent.generateFiles()

        if (runPhpUnitTest && importerUserProvidedPath != null) {
            runPHPUnitTests(outputFolder.parent,"base,import")
        }

        val generatorConfigTests = CodeGeneratorConfig(
                basePackageName = "commercetools/import",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-import-tests")
        )

        val generatorModuleTests = RamlGeneratorModule(importProvider, generatorConfigTests, PhpBaseTypes)
        val generatorComponentTests = RamlGeneratorComponent(generatorModuleTests, PhpTestModule)
        generatorComponentTests.generateFiles()
    }

    @Test
    fun generateMlSdk() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools/ml",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-ml")
        )

        val generatorModule = RamlGeneratorModule(mlProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, PhpModelModule)
        generatorComponent.generateFiles()

        if (runPhpUnitTest && mlUserProvidedPath != null) {
            runPHPUnitTests(outputFolder.parent, "base,ml")
        }

        val generatorConfigTests = CodeGeneratorConfig(
                basePackageName = "commercetools/ml",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-ml-tests")
        )

        val generatorModuleTests = RamlGeneratorModule(mlProvider, generatorConfigTests, PhpBaseTypes)
        val generatorComponentTests = RamlGeneratorComponent(generatorModuleTests, PhpTestModule)
        generatorComponentTests.generateFiles()
    }

    @Test
    fun generatePHPBase() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-base")
        )

        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, PhpBaseModule)
        generatorComponent.generateFiles()
    }

    private fun runPHPUnitTests(workingDir: Path, suite: String = "unit") {
        val process = ProcessBuilder("sh", "-c", "vendor/bin/phpunit --testsuite=$suite")
                .directory(workingDir.toFile())
                .start()

        val output: String = IOUtils.toString(process.inputStream, StandardCharsets.UTF_8)
        Assertions.assertEquals("Unit tests didn't ran successful!\n$output",process.waitFor())
    }
}
