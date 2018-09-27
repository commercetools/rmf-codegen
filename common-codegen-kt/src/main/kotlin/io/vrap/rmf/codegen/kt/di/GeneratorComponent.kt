package io.vrap.rmf.codegen.kt.di

import com.google.inject.Guice
import com.google.inject.Module
import io.vrap.rmf.codegen.kt.rendring.CoreCodeGenerator


class GeneratorComponent(generatorModule: GeneratorModule, vararg modules:  Module) {

    private val injector = Guice.createInjector(listOf(generatorModule).plus(modules))

    private val coreCodeGenerator = injector.getInstance(CoreCodeGenerator::class.java)

    fun generateFiles() = coreCodeGenerator.generate()

}