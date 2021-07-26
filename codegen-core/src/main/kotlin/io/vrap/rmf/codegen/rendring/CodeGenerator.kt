package io.vrap.rmf.codegen.rendring

import io.reactivex.rxjava3.core.Flowable
import io.vrap.rmf.codegen.di.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.Trait
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.types.UnionType
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory

interface CodeGenerator {
    fun generate(): MutableList<Publisher<TemplateFile>>
}

abstract class CodeGeneratorImpl<TRenderer: Renderer<T>, T> constructor(val generators: Set<TRenderer>, private val allTypes: List<T>): CodeGenerator {
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

class ObjectTypeGenerator constructor(generators: Set<ObjectTypeRenderer>, @AllObjectTypes allTypes: List<ObjectType>) : CodeGeneratorImpl<ObjectTypeRenderer, ObjectType>(generators, allTypes)
class StringTypeGenerator constructor(generators: Set<StringTypeRenderer>, @EnumStringTypes allTypes: List<StringType>) : CodeGeneratorImpl<StringTypeRenderer, StringType>(generators, allTypes)
class UnionTypeGenerator constructor(generators: Set<UnionTypeRenderer>, @AllUnionTypes allTypes: List<UnionType>) : CodeGeneratorImpl<UnionTypeRenderer, UnionType>(generators, allTypes)
class PatternStringTypeGenerator constructor(generators: Set<PatternStringTypeRenderer>, @PatternStringTypes allTypes: List<StringType>) : CodeGeneratorImpl<PatternStringTypeRenderer, StringType>(generators, allTypes)
class NamedStringTypeGenerator constructor(generators: Set<NamedStringTypeRenderer>, @NamedScalarTypes allTypes: List<StringType>) : CodeGeneratorImpl<NamedStringTypeRenderer, StringType>(generators, allTypes)
class NamedScalarTypeGenerator<T: AnyType> constructor(generators: Set<NamedScalarTypeRenderer<T>>, @NamedScalarTypes allTypes: List<T>) : CodeGeneratorImpl<NamedScalarTypeRenderer<T>, T>(generators, allTypes)
class ResourceGenerator constructor(generators: Set<ResourceRenderer>, @AllResources allTypes: List<Resource>) : CodeGeneratorImpl<ResourceRenderer, Resource>(generators, allTypes)
class MethodGenerator constructor(generators: Set<MethodRenderer>, @AllResourceMethods allTypes: List<Method>) : CodeGeneratorImpl<MethodRenderer, Method>(generators, allTypes)
class TraitGenerator constructor(generators: Set<TraitRenderer>, @AllTraits allTypes: List<Trait>) : CodeGeneratorImpl<TraitRenderer, Trait>(generators, allTypes)
