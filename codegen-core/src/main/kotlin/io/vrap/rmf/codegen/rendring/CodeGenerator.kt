package io.vrap.rmf.codegen.rendring

import com.google.inject.Inject
import io.reactivex.rxjava3.core.Flowable
import io.vrap.rmf.codegen.di.EnumStringTypes
import io.vrap.rmf.codegen.di.NamedScalarTypes
import io.vrap.rmf.codegen.di.PatternStringTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.Trait
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.types.UnionType
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import kotlin.concurrent.fixedRateTimer

interface CodeGenerator {
    fun generate(): MutableList<Publisher<TemplateFile>>
}

abstract class CodeGeneratorImpl<TRenderer: Renderer<T>, T> @Inject constructor(val generators: Set<TRenderer>, private val allTypes: List<T>): CodeGenerator {
    private val LOGGER = LoggerFactory.getLogger(CodeGenerator::class.java)

    override fun generate(): MutableList<Publisher<TemplateFile>> {
        val templateFiles :MutableList<Publisher<TemplateFile>> = mutableListOf()

        LOGGER.info("generating files with " + this.javaClass.simpleName)
        templateFiles.add(Flowable.fromIterable(generators).flatMap { renderer -> Flowable.fromIterable(allTypes).map { renderer.render(it) } } )

        return templateFiles;
    }
}

class FileGenerator constructor(val fileProducers: Set<FileProducer>): CodeGenerator {
    private val LOGGER = LoggerFactory.getLogger(CodeGenerator::class.java)

    override fun generate(): MutableList<Publisher<TemplateFile>> {
        val templateFiles :MutableList<Publisher<TemplateFile>> = mutableListOf()

        LOGGER.info("generating files with " + this.javaClass.simpleName)
        templateFiles.add(Flowable.fromIterable(fileProducers).flatMap { Flowable.fromIterable(it.produceFiles()) })

        return templateFiles;
    }
}

class ObjectTypeGenerator @Inject constructor(generators: Set<ObjectTypeRenderer>, allTypes: List<ObjectType>) : CodeGeneratorImpl<ObjectTypeRenderer, ObjectType>(generators, allTypes)
class StringTypeGenerator @Inject constructor(generators: Set<StringTypeRenderer>, @EnumStringTypes allTypes: List<StringType>) : CodeGeneratorImpl<StringTypeRenderer, StringType>(generators, allTypes)
class UnionTypeGenerator @Inject constructor(generators: Set<UnionTypeRenderer>, allTypes: List<UnionType>) : CodeGeneratorImpl<UnionTypeRenderer, UnionType>(generators, allTypes)
class PatternStringTypeGenerator @Inject constructor(generators: Set<PatternStringTypeRenderer>, @PatternStringTypes allTypes: List<StringType>) : CodeGeneratorImpl<PatternStringTypeRenderer, StringType>(generators, allTypes)
class NamedScalarTypeGenerator @Inject constructor(generators: Set<NamedScalarTypeRenderer>, @NamedScalarTypes allTypes: List<StringType>) : CodeGeneratorImpl<NamedScalarTypeRenderer, StringType>(generators, allTypes)
class ResourceGenerator @Inject constructor(generators: Set<ResourceRenderer>, allTypes: List<Resource>) : CodeGeneratorImpl<ResourceRenderer, Resource>(generators, allTypes)
class MethodGenerator @Inject constructor(generators: Set<MethodRenderer>, allTypes: List<Method>) : CodeGeneratorImpl<MethodRenderer, Method>(generators, allTypes)
class TraitGenerator @Inject constructor(generators: Set<TraitRenderer>, allTypes: List<Trait>) : CodeGeneratorImpl<TraitRenderer, Trait>(generators, allTypes)
