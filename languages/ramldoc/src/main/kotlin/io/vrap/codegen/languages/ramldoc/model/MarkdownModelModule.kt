package io.vrap.codegen.languages.ramldoc.model

import io.vrap.codegen.languages.ramldoc.extensions.isScalar
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.di.OasGeneratorModule
import io.vrap.rmf.codegen.rendering.*
import io.vrap.rmf.raml.model.types.*

object MarkdownModelModule : Module {

    override fun configure(generatorModule: RamlGeneratorModule) = setOf(
        FileGenerator(
                setOf(
                        MarkdownRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes(), generatorModule.provideModelPackageName()),
                ))
    )
}
