package com.commercetools.rmf.validators

import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.vrap.rmf.raml.validation.RamlValidator
import java.io.File
import java.io.InputStream
import com.google.common.reflect.ClassPath
import java.lang.reflect.Modifier

class ValidatorSetup {
    companion object {
        @JvmStatic
        fun setup(config: File, verbose: Boolean = false): List<RamlValidator> {
            return setup(config.inputStream(), verbose)
        }

        fun setup(config: InputStream, verbose: Boolean = false): List<RamlValidator> {
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
                    rules = mergeRules(rules, sets[apply.set]!!)
                }
            }
            rules = mergeRules(rules, ruleSet.rules.map { it.name to it }.toMap())

            val validators = rules.values.filter { it.enabled }.map { rule -> when(rule.severity != null) {
                    true -> Class.forName(rule.name).getConstructor(RuleSeverity::class.java, List::class.java).newInstance(rule.severity, rule.options)
                    else -> Class.forName(rule.name).getConstructor(RuleSeverity::class.java, List::class.java).newInstance(RuleSeverity.ERROR, rule.options)
                }
            }

            if (verbose) {
                validators.filterIsInstance(DiagnosticsAware::class.java).forEach { validator -> println("${validator::class.java}: ${validator.severity}") }
            }
            return listOf(
                ResolvedResourcesValidator(validators.filterIsInstance( ResolvedResourcesRule::class.java )),
                ResourcesValidator(validators.filterIsInstance( ResourcesRule::class.java )),
                TypesValidator(validators.filterIsInstance( TypesRule::class.java )),
                ModulesValidator(validators.filterIsInstance( ModulesRule::class.java ))
            )
        }

        private fun mergeRules(setOne: Map<String, Rule>, setTwo: Map<String, Rule>): Map<String, Rule> {
            var checks = setOne
            setTwo.forEach { (checkName, check) ->
                if (checks.containsKey(checkName)) {
                    checks = checks.plus(checkName to Rule(checkName, check.severity ?: checks[checkName]!!.severity, checks[checkName]!!.options?.plus(check.options ?: listOf()) ?: check.options, check.enabled && checks[checkName]!!.enabled))
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
                .filterNot { it.getAnnotation(ValidatorSet::class.java) == null && it.getAnnotation(ValidatorSets::class.java) == null }
                .filter { Validator::class.java.isAssignableFrom(it) }
                .flatMap { clazz ->
                    clazz.getAnnotationsByType(ValidatorSet::class.java)
                        .map { it.name to Rule(clazz.name, it.severity, listOf()) }
                }
                .groupBy { it.first }
                .mapValues { it.value.map { it.second.name to it.second }.toMap() };
        }

        public fun allValidatorRules(): List<String> {
            return ClassPath.from(ClassLoader.getSystemClassLoader()).allClasses
                .filter { it.packageName.startsWith("com.commercetools.rmf.validators") }
                .map { classInfo -> classInfo.load() }
                .filter { Validator::class.java.isAssignableFrom(it) }
                .filterNot { Modifier.isAbstract(it.modifiers) }
                .map { it.name }
        }
    }
}
