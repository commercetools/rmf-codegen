package io.vrap.codegen.languages.php

import io.vrap.codegen.languages.php.base.PhpBaseModule
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths


class TestCodeGenerator {

    companion object {
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        val apiProvider: ApiProvider = ApiProvider(apiPath)
        val generatorConfig = CodeGeneratorConfig(basePackageName = "")
    }

    
    fun generatePHPModels() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools/api",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("build/gensrc/commercetools-raml-sdk")
        )

        val generatorModule = GeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PhpModelModule())
        generatorComponent.generateFiles()
    }

    
    fun generatePHPBase() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools",
                outputFolder = Paths.get("build/gensrc/commercetools-raml-base")
        )

        val generatorModule = GeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PhpBaseModule())
        generatorComponent.generateFiles()
    }

    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
