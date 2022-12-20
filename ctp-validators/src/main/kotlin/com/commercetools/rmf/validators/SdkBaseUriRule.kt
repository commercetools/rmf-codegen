package com.commercetools.rmf.validators

import com.damnhandy.uri.template.UriTemplate
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.types.StringInstance
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class SdkBaseUriRule (severity: RuleSeverity, options: List<RuleOption>? = null) : ModulesRule(severity, options) {
    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }
            ?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseApi(api: Api): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (api.baseUri == null) {
            validationResults.add(create(api,"baseUri must not be empty"))
        } else if (api.baseUri.value.variables.isNotEmpty()) {
            val sdkBaseUri = api.getAnnotation("sdkBaseUri")
            if (sdkBaseUri == null) {
                validationResults.add(create(api.baseUri,"sdkBaseUri must be declared as baseUri \"{0}\" contains baseUriParameters", api.baseUri.template))
            } else {
                val sdkBaseUriTemplate = UriTemplate.buildFromTemplate((sdkBaseUri.value as StringInstance).value).build()
                if (sdkBaseUriTemplate.variables.isNotEmpty()) {
                    validationResults.add(create(sdkBaseUri,"sdkBaseUri \"{0}\" must not contain uriParameters", sdkBaseUriTemplate.template))
                }
            }
        }
        return validationResults
    }

    companion object : ValidatorFactory<SdkBaseUriRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): SdkBaseUriRule {
            return SdkBaseUriRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): SdkBaseUriRule {
            return SdkBaseUriRule(severity, options)
        }
    }
}
