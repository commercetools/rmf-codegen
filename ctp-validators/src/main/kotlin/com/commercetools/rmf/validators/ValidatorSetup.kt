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


class ValidatorSetup {
    companion object {
        @JvmStatic
        fun setup(config: File): List<RamlValidator> {
            return setup(config.inputStream())
        }

        fun setup(config: InputStream): List<RamlValidator> {
            val mapper = XmlMapper.builder(XmlFactory(WstxInputFactory(), WstxOutputFactory())).defaultUseWrapper(false)
                .addModule(KotlinModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build()

            val ruleSet = mapper.readValue(config, RuleSet::class.java)

            val validators = ruleSet.rules.map { rule -> Class.forName(rule.name).getConstructor(List::class.java).newInstance(rule.options) }

            return listOf(
                ResolvedResourcesValidator(validators.filterIsInstance( ResolvedResourcesRule::class.java )),
                ResourcesValidator(validators.filterIsInstance( ResourcesRule::class.java )),
                TypesValidator(validators.filterIsInstance( TypesRule::class.java )),
                ModulesValidator(validators.filterIsInstance( ModulesRule::class.java ))
            )
        }
    }
}
