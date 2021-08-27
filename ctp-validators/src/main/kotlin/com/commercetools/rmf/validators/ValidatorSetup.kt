package com.commercetools.rmf.validators

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vrap.rmf.raml.model.modules.util.ModulesSwitch
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import io.vrap.rmf.raml.validation.RamlValidator
import org.eclipse.emf.common.util.Diagnostic
import java.io.File


class ValidatorSetup {
    companion object {
        @JvmStatic
        fun setup(config: File): List<RamlValidator> {
            val mapper = XmlMapper.builder().defaultUseWrapper(false)
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
