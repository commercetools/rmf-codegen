package com.commercetools.rmf.validators

import io.vrap.rmf.nodes.antlr.NodeToken
import io.vrap.rmf.nodes.antlr.NodeTokenProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.persistence.antlr.RAMLParser
import io.vrap.rmf.raml.persistence.antlr.RamlNodeTokenSource
import io.vrap.rmf.raml.persistence.constructor.RamlParserAdapter
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode
import org.eclipse.emf.common.util.Diagnostic

class FilenameRule(options: List<RuleOption>? = null) : ModulesRule(options) {
    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.toLowerCase() == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseApi(api: Api?): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        if (api?.types != null) {
            api.types
                .forEach {
                    val r = it?.eAdapters()?.filterIsInstance(RamlParserAdapter::class.java)?.map {
                            adapter -> checkIncludedTypeFileName(it, adapter.parserRuleContext)
                    } ?: emptyList()
                    if (r.contains(true).not()) {
                        validationResults.add(error(it, "Type {0} must have the same file name as type itself", it.name))
                    }
                }
        }
        return validationResults
    }

    private fun checkIncludedTypeFileName(type: AnyType, context: ParserRuleContext): Boolean {
        if (context is RAMLParser.TypeDeclarationMapContext) {
            val id = context.name
            val idIndex = context.children.indexOf(context.name)
            val nextNode = context.children.getOrNull(idIndex + 1)
            if (nextNode is TerminalNode && (nextNode.symbol as NodeToken).location != (id.start as NodeToken).location) {
                return (nextNode.symbol as NodeToken).location.contains(type.name + ".raml")
            }
        }

        return true
    }

    companion object : ValidatorFactory<FilenameRule> {
        private val defaultExcludes by lazy { listOf("") }

        override fun create(options: List<RuleOption>): FilenameRule {
            return FilenameRule(options)
        }
    }
}
