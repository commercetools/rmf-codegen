package io.vrap.rmf.codegen.kt.di

import com.google.inject.Guice


class GeneratorComponent(generatorModule: GeneratorModule) {

    val injector = Guice.createInjector(generatorModule)

}