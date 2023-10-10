package io.vrap.rmf.codegen.rendering

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.vrap.rmf.codegen.io.DataSink
import io.vrap.rmf.codegen.io.TemplateFile
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory

class CoreCodeGenerator constructor(val dataSink: DataSink, val gitHash: String, val generators: Set<CodeGenerator>) {

    private val LOGGER = LoggerFactory.getLogger(CoreCodeGenerator::class.java)
    private val PARALLELISM = Runtime.getRuntime().availableProcessors()

    fun generate() {

        if (!dataSink.dryRun()) {
            if (dataSink.clean()) {
                LOGGER.info("data sink cleanup successful")
            } else {
                LOGGER.info("data sink cleanup unsuccessful")
            }
        }

        val templateFiles :MutableList<Publisher<TemplateFile>> = mutableListOf()

        if (gitHash.isNotBlank()) {
            templateFiles.add(Flowable.just(TemplateFile(relativePath = "gen.properties", content = """
                hash=${gitHash}
            """.trimIndent())))
        }

        templateFiles.addAll(generators.flatMap { generator -> generator.generate() })

        if (!dataSink.dryRun()) {
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
        }

        LOGGER.info("files generation ended")
    }

}
