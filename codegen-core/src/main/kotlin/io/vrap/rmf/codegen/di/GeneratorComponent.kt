package io.vrap.rmf.codegen.di

import com.google.inject.Guice
import com.google.inject.Module
import io.vrap.rmf.codegen.rendring.CoreCodeGenerator


class GeneratorComponent(generatorModule: GeneratorModule, vararg modules:  Module) {

    private val injector = Guice.createInjector(listOf(generatorModule).plus(modules))

    private val coreCodeGenerator = injector.getInstance(CoreCodeGenerator::class.java)

    fun generateFiles() = coreCodeGenerator.generate()

}
