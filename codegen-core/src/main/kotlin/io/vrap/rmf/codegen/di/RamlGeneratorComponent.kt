package io.vrap.rmf.codegen.di

import io.vrap.rmf.codegen.rendering.CoreCodeGenerator


class RamlGeneratorComponent(generatorModule: RamlGeneratorModule, vararg modules:  Module): GeneratorComponent {

    private val coreCodeGenerator = CoreCodeGenerator(
                generatorModule.dataSink(),
                generatorModule.provideGitHash(),
                modules.flatMap { module -> module.configure(generatorModule) }.toSet()
        )

    override fun generateFiles() = coreCodeGenerator.generate()
}
