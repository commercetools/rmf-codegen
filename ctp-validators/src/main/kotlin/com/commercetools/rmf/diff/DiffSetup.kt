package com.commercetools.rmf.diff

import com.commercetools.rmf.validators.Validator
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.common.reflect.ClassPath
import com.google.common.reflect.ClassPath.ClassInfo
import io.vrap.rmf.raml.validation.RamlValidator
import java.io.File
import java.io.InputStream
import kotlin.jvm.internal.Reflection


class DiffSetup {
    companion object {
        @JvmStatic
        fun setup(config: File): List<Differ<Any>> {
            return setup(config.inputStream())
        }

        @JvmStatic
        fun setup(config: InputStream): List<Differ<Any>> {
            val mapper = XmlMapper.builder(XmlFactory(WstxInputFactory(), WstxOutputFactory())).defaultUseWrapper(false)
                .addModule(KotlinModule.Builder().build())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build()
            val sets = readSets()

            val ruleSet = mapper.readValue(config, DiffConfiguration::class.java)

            var checks = mapOf<String, Check>()
            ruleSet.apply.forEach { apply ->
                run {
                    checks = mergeCheck(checks, sets[apply.set]!!)
                }
            }
            checks = mergeCheck(checks, ruleSet.checks.map { it.name to it }.toMap())

            return checks.values.map { rule -> Class.forName(rule.name).getConstructor(CheckSeverity::class.java).newInstance(rule.severity) as Differ<Any> }
        }

        private fun mergeCheck(setOne: Map<String, Check>, setTwo: Map<String, Check>): Map<String, Check> {
            var checks = setOne
            setTwo.forEach { (checkName, check) ->
                if (checks.containsKey(checkName)) {
                    checks = checks.plus(checkName to Check(checkName, check.severity, setOne[checkName]!!.options.plus(check.options)))
                } else {
                    checks = checks.plus(checkName to check)
                }
            }

            return checks
        }

        private fun readSets(): Map<String, Map<String, Check>> {
            return ClassPath.from(ClassLoader.getSystemClassLoader()).allClasses
                .filter { it.packageName.startsWith("com.commercetools.rmf.diff") }
                .map { classInfo -> classInfo.load() }
                .filterNot { it.getAnnotation(DiffSet::class.java) == null && it.getAnnotation(DiffSets::class.java) == null }
                .filter { DiffCheck::class.java.isAssignableFrom(it) }
                .flatMap { clazz ->
                    clazz.getAnnotationsByType(DiffSet::class.java)
                        .map { it.name to Check(clazz.name, it.severity, listOf()) }
                }
                .groupBy { it.first }
                .mapValues { it.value.map { it.second.name to it.second }.toMap() };
        }
    }
}
