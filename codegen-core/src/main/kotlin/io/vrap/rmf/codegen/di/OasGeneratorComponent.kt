package io.vrap.rmf.codegen.di

import io.vrap.rmf.codegen.rendring.CoreCodeGenerator

class OasGeneratorComponent(generatorModule: OasGeneratorModule, vararg modules:  Module): GeneratorComponent {

    private val coreCodeGenerator = CoreCodeGenerator(
        generatorModule.dataSink(),
        generatorModule.provideGitHash(),
        modules.flatMap { module -> module.configure(generatorModule) }.toSet()
    )

    override fun generateFiles() = coreCodeGenerator.generate()

}
