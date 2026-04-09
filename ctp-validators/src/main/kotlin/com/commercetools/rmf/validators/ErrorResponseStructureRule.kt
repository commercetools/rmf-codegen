package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class ErrorResponseStructureRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseMethod(method: Method): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()
        val resource = method.eContainer() as Resource
        val methodId = "${method.method.name} ${resource.fullUri.template}"

        if (exclude.contains(methodId)) {
            return validationResults
        }

        val errorResponses = method.responses.filter { response -> response.statusCode.toInt() >= 400 }

        errorResponses.forEach { response ->
            val statusCode = response.statusCode
            response.bodies.forEach { body ->
                val bodyType = body.type
                if (bodyType is ObjectType) {
                    if (bodyType.properties.none { it.name == "statusCode" }) {
                        validationResults.add(create(method, "Method \"{0} {1}\" error response (status {2}) body type must have a \"statusCode\" property", method.method.name, resource.fullUri.template, statusCode))
                    }

                    if (bodyType.properties.none { it.name == "message" }) {
                        validationResults.add(create(method, "Method \"{0} {1}\" error response (status {2}) body type must have a \"message\" property", method.method.name, resource.fullUri.template, statusCode))
                    }

                    val errorsProperty = bodyType.properties.find { it.name == "errors" }
                    if (errorsProperty == null) {
                        validationResults.add(warning(method, "Method \"{0} {1}\" error response (status {2}) body type should have an \"errors\" array property", method.method.name, resource.fullUri.template, statusCode))
                    } else if (errorsProperty.type is ArrayType) {
                        val itemsType = (errorsProperty.type as ArrayType).items
                        if (itemsType is ObjectType) {
                            if (itemsType.properties.none { it.name == "code" }) {
                                validationResults.add(create(method, "Method \"{0} {1}\" error response (status {2}) error items must have a \"code\" property", method.method.name, resource.fullUri.template, statusCode))
                            }
                            if (itemsType.properties.none { it.name == "message" }) {
                                validationResults.add(create(method, "Method \"{0} {1}\" error response (status {2}) error items must have a \"message\" property", method.method.name, resource.fullUri.template, statusCode))
                            }
                        }
                    }
                }
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<ErrorResponseStructureRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): ErrorResponseStructureRule {
            return ErrorResponseStructureRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): ErrorResponseStructureRule {
            return ErrorResponseStructureRule(severity, options)
        }
    }
}
