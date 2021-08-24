package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.BodyContainer
import io.vrap.rmf.raml.model.types.BuiltinType
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.ecore.EObject
import java.util.*

class NamedBodyTypeRule(options: List<RuleOption>? = null) : ResourcesRule(options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

//    override fun caseMethod(method: Method?): List<Diagnostic> {
//        val validationResults: MutableList<Diagnostic> = ArrayList()
//        val httpMethod = method?.method
//
//        if (httpMethod == HttpMethod.POST && !method.bodies.all { body -> body.type != null } && exclude.contains("${method.method.name} ${(method.eContainer() as Resource).fullUri.template}").not()) {
//            validationResults.add(error(method, "Method \"{0} {1}\" must have body type defined", method.method.name, (method.eContainer() as Resource).fullUri.template))
//        }
//        return validationResults
//    }

    override fun caseBodyContainer(bodyContainer: BodyContainer): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (!bodyContainer.bodies.all { body -> body.type != null && body.type.name != null && !BuiltinType.of(body.type.name).isPresent }) {
            if (bodyContainer is Method) {
                validationResults.add(
                    error(
                        bodyContainer,
                        "Method \"{0} {1}\" must have body type defined",
                        bodyContainer.method.name,
                        (bodyContainer.eContainer() as Resource).fullUri.template
                    )
                )
            } else {
                val method = bodyContainer.getParent(Method::class.java)
                validationResults.add(
                    error(
                        bodyContainer,
                        "Method \"{0} {1}\" must have success body type defined",
                        method?.method?.name,
                        (method?.eContainer() as Resource).fullUri.template
                    )
                )
            }

        }

        return validationResults
    }

    companion object : ValidatorFactory<NamedBodyTypeRule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): NamedBodyTypeRule {
            return NamedBodyTypeRule(options)
        }
    }

    fun <T> EObject.getParent(parentClass: Class<T>): T? {
        if (this.eContainer() == null) {
            return null
        }
        return if (parentClass.isInstance(this.eContainer())) {
            this.eContainer() as T
        } else this.eContainer().getParent(parentClass)
    }
}
