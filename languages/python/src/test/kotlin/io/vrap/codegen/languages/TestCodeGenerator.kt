package io.vrap.codegen.languages

import io.vrap.codegen.languages.python.client.PythonClientModule
import io.vrap.codegen.languages.python.model.PythonBaseTypes
import io.vrap.codegen.languages.python.model.PythonModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths

class TestPythonCodeGenerator {

    companion object {
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val apiPath: Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        val apiProvider: RamlApiProvider = RamlApiProvider(apiPath)
        val generatorConfig = CodeGeneratorConfig(basePackageName = "")
    }

    @Test
    fun generatPyModels() {
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, PythonBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, PythonModelModule, PythonClientModule)
        generatorComponent.generateFiles()
    }

    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
