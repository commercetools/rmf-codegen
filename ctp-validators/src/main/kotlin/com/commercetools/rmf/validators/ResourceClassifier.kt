package com.commercetools.rmf.validators

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.Literal
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Resource

/**
 * Classification categories for API resource path segments.
 *
 * Each category represents a different semantic role a path segment can play:
 * - COLLECTION: a resource collection (e.g. /products, /orders) — must be plural
 * - IDENTIFIED_OBJECT: an identified instance (e.g. /{id}, /key={key}) — skip
 * - SCOPING_PREFIX: a scoping prefix (e.g. /me, /in-store, /as-associate) — skip
 * - ACTION_ENDPOINT: an action verb endpoint (e.g. /login, /search, /replicate) — skip
 * - MATCHING_ENDPOINT: a matching criteria endpoint (e.g. /matching-cart) — skip
 * - IDENTIFIER_LOOKUP: a lookup by identifier (e.g. /email-token={token}) — skip
 * - SINGLETON: a singleton resource (e.g. /graphql, /active-cart) — must be singular
 * - UNKNOWN: unclassifiable — skip (safety fallback)
 */
enum class ResourceCategory {
    COLLECTION,
    IDENTIFIED_OBJECT,
    SCOPING_PREFIX,
    ACTION_ENDPOINT,
    MATCHING_ENDPOINT,
    IDENTIFIER_LOOKUP,
    SINGLETON,
    UNKNOWN
}

/**
 * Classifies a RAML [Resource] into a [ResourceCategory] based on structural signals.
 *
 * Classification logic (order matters — first match wins):
 * 1. URI literal contains `=` → IDENTIFIER_LOOKUP
 * 2. URI is purely `/{variable}` → IDENTIFIED_OBJECT
 * 3. resourcePathName starts with `as-` or `in-`, or equals `me` → SCOPING_PREFIX
 * 4. resourcePathName starts with `matching-` → MATCHING_ENDPOINT
 * 5. Resource type name is `baseDomain` → COLLECTION
 * 6. Resource type name is `baseResource` → IDENTIFIED_OBJECT
 * 7. Has child resource with `/{variable}` or `/key={key}` URI → COLLECTION
 * 8. resourcePathName matches action verb whitelist, OR is a POST-only leaf → ACTION_ENDPOINT
 * 9. Has GET, no identified-object children → SINGLETON
 * 10. Has non-trivial children → COLLECTION
 * 11. Otherwise → UNKNOWN
 */
object ResourceClassifier {

    private val defaultActionVerbs: Set<String> by lazy {
        val stream = ResourceClassifier::class.java.getResourceAsStream("/default-action-verbs.txt")
        stream?.bufferedReader()?.readLines()
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() && !it.startsWith("#") }
            ?.toSet()
            ?: emptySet()
    }

    /**
     * Classifies the given [resource] into a [ResourceCategory].
     *
     * @param resource the RAML resource to classify
     * @param additionalActionVerbs extra action verbs to merge with the defaults (e.g. from ruleset.xml)
     * @return the classification category
     */
    fun classify(resource: Resource, additionalActionVerbs: List<String> = emptyList()): ResourceCategory {
        val resourcePathName = resource.resourcePathName ?: return ResourceCategory.UNKNOWN

        // Step 1: URI literal contains "=" → IDENTIFIER_LOOKUP
        val literals = resource.relativeUri.components.filterIsInstance(Literal::class.java)
        if (literals.any { it.value.contains("=") }) {
            return ResourceCategory.IDENTIFIER_LOOKUP
        }

        // Step 2: URI is purely /{variable} (one "/" literal + one expression) → IDENTIFIED_OBJECT
        val componentsList = resource.relativeUri.components.toList()
        if (componentsList.size == 2
            && componentsList[0] is Literal && (componentsList[0] as Literal).value == "/"
            && componentsList[1] is Expression) {
            return ResourceCategory.IDENTIFIED_OBJECT
        }

        // Step 3: Scoping prefix (as-*, in-*, me)
        if (resourcePathName.startsWith("as-") || resourcePathName.startsWith("in-") || resourcePathName == "me") {
            return ResourceCategory.SCOPING_PREFIX
        }

        // Step 4: Matching endpoint (matching-* pattern)
        if (resourcePathName.startsWith("matching-")) {
            return ResourceCategory.MATCHING_ENDPOINT
        }

        // Step 5 & 6: Resource type name
        val typeName = resource.type?.type?.name
        if (typeName == "baseDomain") {
            return ResourceCategory.COLLECTION
        }
        if (typeName == "baseResource") {
            return ResourceCategory.IDENTIFIED_OBJECT
        }

        // Step 7: Has child with /{variable} or /key={key} → COLLECTION
        if (hasIdentifiedObjectChild(resource)) {
            return ResourceCategory.COLLECTION
        }

        // Step 8: Action verb whitelist or POST-only leaf
        val allActionVerbs = defaultActionVerbs + additionalActionVerbs.toSet()
        if (allActionVerbs.contains(resourcePathName)) {
            return ResourceCategory.ACTION_ENDPOINT
        }
        if (isPostOnlyLeaf(resource)) {
            return ResourceCategory.ACTION_ENDPOINT
        }

        // Step 9: Has GET, no identified-object children → SINGLETON
        if (resource.getMethod(HttpMethod.GET) != null && !hasIdentifiedObjectChild(resource)) {
            return ResourceCategory.SINGLETON
        }

        // Step 10: Has non-trivial children → COLLECTION
        if (resource.resources != null && resource.resources.isNotEmpty()) {
            return ResourceCategory.COLLECTION
        }

        // Step 11: Fallback
        return ResourceCategory.UNKNOWN
    }

    /**
     * Checks if the resource has a child that looks like an identified object:
     * - `/{variable}` pattern (single expression after `/`)
     * - `/key={key}` pattern (literal with `=`)
     */
    private fun hasIdentifiedObjectChild(resource: Resource): Boolean {
        if (resource.resources == null) return false
        return resource.resources.any { child ->
            val childComponentsList = child.relativeUri.components.toList()
            // /{variable} pattern
            val isIdChild = childComponentsList.size == 2
                && childComponentsList[0] is Literal && (childComponentsList[0] as Literal).value == "/"
                && childComponentsList[1] is Expression
            // /key={key} pattern
            val isKeyChild = childComponentsList.filterIsInstance(Literal::class.java)
                .any { it.value.contains("=") }
            isIdChild || isKeyChild
        }
    }

    /**
     * Checks if the resource is a POST-only leaf (no children, only POST method).
     */
    private fun isPostOnlyLeaf(resource: Resource): Boolean {
        val hasChildren = resource.resources != null && resource.resources.isNotEmpty()
        if (hasChildren) return false
        val methods = resource.methods ?: return false
        return methods.size == 1 && methods.first().method == HttpMethod.POST
    }
}
