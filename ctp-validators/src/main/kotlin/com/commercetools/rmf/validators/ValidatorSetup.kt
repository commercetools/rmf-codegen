package com.commercetools.rmf.validators

import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vrap.rmf.raml.validation.RamlValidator
import java.io.File
import java.io.InputStream
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.common.reflect.ClassPath

class ValidatorSetup {
    companion object {
        @JvmStatic
        fun setup(config: File): List<RamlValidator> {
            return setup(config.inputStream())
        }

        fun setup(config: InputStream): List<RamlValidator> {
            val mapper = XmlMapper.builder(XmlFactory(WstxInputFactory(), WstxOutputFactory())).defaultUseWrapper(false)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build()
//                .registerKotlinModule()

            val sets = readSets()

            val ruleSet = mapper.readValue(config, RuleSet::class.java)
            var rules = mapOf<String, Rule>()
            ruleSet.apply.forEach { apply ->
                run {
                    rules = mergeCheck(rules, sets[apply.set]!!)
                }
            }
            rules = mergeCheck(rules, ruleSet.rules.map { it.name to it }.toMap())


            val validators = rules.values.map { rule -> when(rule.severity != null) {
                    true -> Class.forName(rule.name).getConstructor(RuleSeverity::class.java, List::class.java).newInstance(rule.severity, rule.options)
                    else -> Class.forName(rule.name).getConstructor(RuleSeverity::class.java, List::class.java).newInstance(RuleSeverity.ERROR, rule.options)
                }
            }

            return listOf(
                ResolvedResourcesValidator(validators.filterIsInstance( ResolvedResourcesRule::class.java )),
                ResourcesValidator(validators.filterIsInstance( ResourcesRule::class.java )),
                TypesValidator(validators.filterIsInstance( TypesRule::class.java )),
                ModulesValidator(validators.filterIsInstance( ModulesRule::class.java ))
            )
        }

        private fun mergeCheck(setOne: Map<String, Rule>, setTwo: Map<String, Rule>): Map<String, Rule> {
            var checks = setOne
            setTwo.forEach { (checkName, check) ->
                if (checks.containsKey(checkName)) {
                    checks = checks.plus(checkName to Rule(checkName, check.severity, checks[checkName]!!.options?.plus(check.options ?: listOf()) ?: check.options))
                } else {
                    checks = checks.plus(checkName to check)
                }
            }

            return checks
        }

        private fun readSets(): Map<String, Map<String, Rule>> {
            return ClassPath.from(ClassLoader.getSystemClassLoader()).allClasses
                .filter { it.packageName.startsWith("com.commercetools.rmf.validators") }
                .map { classInfo -> classInfo.load() }
                .filterNot { it.getAnnotation(RulesSet::class.java) == null && it.getAnnotation(RulesSets::class.java) == null }
                .flatMap { clazz ->
                    clazz.getAnnotationsByType(RulesSet::class.java)
                        .map { it.name to Rule(clazz.name, it.severity, listOf()) }
                }
                .groupBy { it.first }
                .mapValues { it.value.map { it.second.name to it.second }.toMap() };
        }
    }
}
