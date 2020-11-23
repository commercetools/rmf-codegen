package io.vrap.rmf.codegen.rendring

import com.google.inject.Inject
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.vrap.rmf.codegen.common.generator.core.ResourceCollection
import io.vrap.rmf.codegen.di.ApiGitHash
import io.vrap.rmf.codegen.di.EnumStringTypes
import io.vrap.rmf.codegen.di.NamedScalarTypes
import io.vrap.rmf.codegen.di.PatternStringTypes
import io.vrap.rmf.codegen.io.DataSink
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.types.UnionType
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory

class CoreCodeGenerator @Inject constructor(val dataSink: DataSink,
                                            private val allObjectTypes: MutableList<ObjectType>,
                                            private val allUnionTypes: MutableList<UnionType>,
                                            @EnumStringTypes private val allEnumStringTypes : MutableList<StringType>,
                                            @PatternStringTypes private val allPatternStringTypes : MutableList<StringType>,
                                            @NamedScalarTypes private val allNamedScalarTypes: MutableList<StringType>,
                                            private val allResourceCollections: MutableList<ResourceCollection>,
                                            private val allResourceMethods: MutableList<Method>,
                                            private val allResources: MutableList<Resource>
                                            ) {

    private val LOGGER = LoggerFactory.getLogger(CoreCodeGenerator::class.java)
    private val PARALLELISM = 100

    @Inject(optional = true)
    lateinit var objectTypeGenerators: MutableSet<ObjectTypeRenderer>

    @Inject(optional = true)
    lateinit var unionTypeGenerators: MutableSet<UnionTypeRenderer>

    @Inject(optional = true)
    lateinit var enumStringTypeGenerators: MutableSet<StringTypeRenderer>

    @Inject(optional = true)
    lateinit var patternStringTypeGenerators: MutableSet<PatternStringTypeRenderer>

    @Inject(optional = true)
    lateinit var namedScalarTypeGenerators: MutableSet<NamedScalarTypeRenderer>

    @Inject(optional = true)
    lateinit var allResourcesGenerators: MutableSet<ResourceCollectionRenderer>

    @Inject(optional = true)
    lateinit var allResourceMethodGenerators: MutableSet<MethodRenderer>

    @Inject(optional = true)
    lateinit var allResourceGenerators: MutableSet<ResourceRenderer>

    @Inject(optional = true)
    lateinit var fileProducers: MutableSet<FileProducer>

    @Inject(optional = true)
    @ApiGitHash
    lateinit var gitHash: String

    fun generate() {

        if(dataSink.clean()){
            LOGGER.info("data sink cleanup successful")
        } else {
            LOGGER.info("data sink cleanup unsuccessful")
        }

        val templateFiles :MutableList<Publisher<TemplateFile>> = mutableListOf()

        templateFiles.add(Flowable.just(TemplateFile( relativePath = "gen.properties", content = """
            hash=${gitHash}
        """.trimIndent())))

        if (::objectTypeGenerators.isInitialized) {
            LOGGER.info("generating files for object types")
            templateFiles.add(Flowable.fromIterable(objectTypeGenerators).flatMap { renderer -> Flowable.fromIterable(allObjectTypes).map { renderer.render(it) } } )
        }

        if (::unionTypeGenerators.isInitialized) {
            LOGGER.info("generating files for union types")
            templateFiles.add(Flowable.fromIterable(unionTypeGenerators).flatMap { renderer -> Flowable.fromIterable(allUnionTypes).map { renderer.render(it) } } )

        }

        if (::enumStringTypeGenerators.isInitialized) {
            LOGGER.info("generating files for enum string types")
            templateFiles.add(Flowable.fromIterable(enumStringTypeGenerators).flatMap { renderer -> Flowable.fromIterable(allEnumStringTypes).map { renderer.render(it) } } )
        }

        if (::patternStringTypeGenerators.isInitialized) {
            LOGGER.info("generating files for pattern string types")
            templateFiles.add(Flowable.fromIterable(patternStringTypeGenerators).flatMap { renderer -> Flowable.fromIterable(allPatternStringTypes).map { renderer.render(it) } } )
        }

        if (::namedScalarTypeGenerators.isInitialized) {
            LOGGER.info("generating files for named string types")
            templateFiles.add(Flowable.fromIterable(namedScalarTypeGenerators).flatMap { renderer -> Flowable.fromIterable(allNamedScalarTypes).map { renderer.render(it) } } )
        }

        if (::allResourcesGenerators.isInitialized) {
            LOGGER.info("generating files for resource collections")
            templateFiles.add(Flowable.fromIterable(allResourcesGenerators).flatMap { renderer -> Flowable.fromIterable(allResourceCollections).map { renderer.render(it) } } )
        }

        if (::allResourceMethodGenerators.isInitialized) {
            LOGGER.info("generating files for resource methods")
            templateFiles.add(Flowable.fromIterable(allResourceMethodGenerators).flatMap { renderer -> Flowable.fromIterable(allResourceMethods).map { renderer.render(it) } } )
        }

        if (::allResourceGenerators.isInitialized) {
            LOGGER.info("generating files for resource methods")
            templateFiles.add(Flowable.fromIterable(allResourceGenerators).flatMap { renderer -> Flowable.fromIterable(allResources).map { renderer.render(it) } } )
        }

        if(::fileProducers.isInitialized){
            LOGGER.info("generating types for file producers")
            fileProducers.flatMap { it.produceFiles() }.map { dataSink.write(it) }
            templateFiles.add(Flowable.fromIterable(fileProducers).flatMap { Flowable.fromIterable(it.produceFiles()) } )
        }

        Flowable.concat(templateFiles)
                .observeOn(Schedulers.io())
                .parallel(PARALLELISM)
                .map { dataSink.write(it) }
                .sequential()
                .blockingSubscribe(
                        {},
                        { error -> LOGGER.error("Error occured while generating files",error)}
                )

        dataSink.postClean()

        LOGGER.info("files generation ended")
    }

}
