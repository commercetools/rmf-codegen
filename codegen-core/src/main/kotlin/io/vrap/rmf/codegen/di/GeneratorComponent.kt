package io.vrap.rmf.codegen.di

import io.vrap.rmf.codegen.io.FileDataSink
import io.vrap.rmf.codegen.rendring.CoreCodeGenerator


class GeneratorComponent(generatorModule: GeneratorModule, vararg modules:  Module) {

    private val coreCodeGenerator = CoreCodeGenerator(
                generatorModule.dataSink(),
                generatorModule.provideGitHash(),
                modules.flatMap { module -> module.configure(generatorModule) }.toSet()
        )

    fun generateFiles() = coreCodeGenerator.generate()

}
