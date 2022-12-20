package com.commercetools.rmf.diff

import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.InputStream


class DiffSetup {
    companion object {
        @JvmStatic
        fun setup(config: File): List<Differ<Any>> {
            return setup(config.inputStream())
        }

        fun setup(config: InputStream): List<Differ<Any>> {
            val mapper = XmlMapper.builder(XmlFactory(WstxInputFactory(), WstxOutputFactory())).defaultUseWrapper(false)
                .addModule(KotlinModule.Builder().build())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build()

            val ruleSet = mapper.readValue(config, DiffConfiguration::class.java)

            return ruleSet.checks.map { rule -> Class.forName(rule.name).getConstructor(CheckSeverity::class.java).newInstance(rule.severity) as Differ<Any> }
        }
    }
}
