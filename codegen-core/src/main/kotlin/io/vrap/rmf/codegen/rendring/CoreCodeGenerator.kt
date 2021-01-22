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

class CoreCodeGenerator @Inject constructor(val dataSink: DataSink, val gitHash: String, val generators: Set<CodeGenerator>) {

    private val LOGGER = LoggerFactory.getLogger(CoreCodeGenerator::class.java)
    private val PARALLELISM = 100

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

        templateFiles.addAll(generators.flatMap { generator -> generator.generate() })

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
