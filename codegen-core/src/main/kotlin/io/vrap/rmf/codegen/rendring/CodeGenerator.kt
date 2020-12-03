package io.vrap.rmf.codegen.rendring

import com.google.inject.Inject
import io.reactivex.rxjava3.core.Flowable
import io.vrap.rmf.codegen.di.EnumStringTypes
import io.vrap.rmf.codegen.di.NamedScalarTypes
import io.vrap.rmf.codegen.di.PatternStringTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import kotlin.concurrent.fixedRateTimer

interface CodeGenerator {
    fun generate(): MutableList<Publisher<TemplateFile>>
}

abstract class CodeGeneratorImpl<TRenderer: Renderer<T>, T> @Inject constructor(private val allTypes: MutableList<T>): CodeGenerator {
    private val LOGGER = LoggerFactory.getLogger(CodeGenerator::class.java)

    @Inject(optional = true)
    lateinit var generators: MutableSet<TRenderer>

    override fun generate(): MutableList<Publisher<TemplateFile>> {
        val templateFiles :MutableList<Publisher<TemplateFile>> = mutableListOf()

        if (::generators.isInitialized) {
            LOGGER.info("generating files with " + this.javaClass.simpleName)
            templateFiles.add(Flowable.fromIterable(generators).flatMap { renderer -> Flowable.fromIterable(allTypes).map { renderer.render(it) } } )
        }

        return templateFiles;
    }
}

class FileGenerator: CodeGenerator {
    private val LOGGER = LoggerFactory.getLogger(CodeGenerator::class.java)

    @Inject(optional = true)
    lateinit var fileProducers: MutableSet<FileProducer>

    override fun generate(): MutableList<Publisher<TemplateFile>> {
        val templateFiles :MutableList<Publisher<TemplateFile>> = mutableListOf()

        if (::fileProducers.isInitialized) {
            LOGGER.info("generating files with " + this.javaClass.simpleName)
            templateFiles.add(Flowable.fromIterable(fileProducers).flatMap { Flowable.fromIterable(it.produceFiles()) })
        }

        return templateFiles;
    }
}
class ObjectTypeGenerator @Inject constructor(allTypes: MutableList<ObjectType>) : CodeGeneratorImpl<ObjectTypeRenderer, ObjectType>(allTypes)
class StringTypeGenerator @Inject constructor(@EnumStringTypes allTypes: MutableList<StringType>) : CodeGeneratorImpl<StringTypeRenderer, StringType>(allTypes)
class PatternStringTypeGenerator @Inject constructor(@PatternStringTypes allTypes: MutableList<StringType>) : CodeGeneratorImpl<PatternStringTypeRenderer, StringType>(allTypes)
class NamedScalarTypeGenerator @Inject constructor(@NamedScalarTypes allTypes: MutableList<StringType>) : CodeGeneratorImpl<NamedScalarTypeRenderer, StringType>(allTypes)
class ResourceGenerator @Inject constructor(allTypes: MutableList<Resource>) : CodeGeneratorImpl<ResourceRenderer, Resource>(allTypes)
class MethodTypeGenerator @Inject constructor(allTypes: MutableList<Method>) : CodeGeneratorImpl<MethodRenderer, Method>(allTypes)
