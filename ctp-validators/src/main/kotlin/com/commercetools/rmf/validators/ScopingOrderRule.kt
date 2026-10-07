package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.Resource
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

/**
 * Validates that scoping prefixes in a resource path appear in the correct composition order:
 * `as-*` → `in-*` → `me`
 *
 * Not all prefixes need to be present — only those that appear must be in the correct relative order.
 * Only fires on resources that have methods defined (to avoid duplicate diagnostics on intermediate nodes).
 */
@ValidatorSet
class ScopingOrderRule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseResource(resource: Resource): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        // Only check resources that have methods (to avoid duplicates on intermediate nodes)
        if (resource.methods == null || resource.methods.isEmpty()) return emptyList()

        val fullUri = resource.fullUri?.template ?: return emptyList()
        if (exclude.contains(fullUri)) return emptyList()

        val segments = fullUri.split("/").filter { it.isNotEmpty() }

        // Extract scoping prefix positions
        val scopingPositions = mutableListOf<Pair<Int, String>>()
        segments.forEach { segment ->
            // Remove any URI template expressions for comparison
            val clean = segment.replace(Regex("\\{[^}]+}"), "").trimEnd('=')
            when {
                clean.startsWith("as-") -> scopingPositions.add(Pair(SCOPE_ORDER_AS, "as-* ($clean)"))
                clean.startsWith("in-") -> scopingPositions.add(Pair(SCOPE_ORDER_IN, "in-* ($clean)"))
                clean == "me" -> scopingPositions.add(Pair(SCOPE_ORDER_ME, "me"))
            }
        }

        // Check that the scoping prefixes are in non-decreasing order
        for (i in 1 until scopingPositions.size) {
            val prev = scopingPositions[i - 1]
            val curr = scopingPositions[i]
            if (prev.first > curr.first) {
                validationResults.add(create(resource,
                    "Resource \"{0}\" has incorrect scoping order: \"{1}\" must appear before \"{2}\"",
                    fullUri, curr.second, prev.second))
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<ScopingOrderRule> {
        private const val SCOPE_ORDER_AS = 1
        private const val SCOPE_ORDER_IN = 2
        private const val SCOPE_ORDER_ME = 3

        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): ScopingOrderRule {
            return ScopingOrderRule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): ScopingOrderRule {
            return ScopingOrderRule(severity, options)
        }
    }
}
