package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.apache.commons.lang3.StringEscapeUtils
import java.util.*
import kotlin.reflect.KFunction1

open class ItemGenModel(private val resource: Resource, val template: KFunction1<ItemGenModel, String>, private val method: Method) {

    val url = PostmanUrl(resource, method)

    fun method() = method.methodName.toUpperCase()

    val name: String
        get() =
            StringCaseFormat.UPPER_CAMEL_CASE.apply(Optional.ofNullable(resource.displayName).map<String> { it.value }.orElse(resource.resourcePathName))

    open val description: String
        get() {
            val description = Optional.ofNullable(method.description).map<String> { it.value }.orElse(method.method.getName() + " " + name)
            return StringEscapeUtils.escapeJson(description)
        }

    fun description(): String {
        return description.escapeJson()?.escapeAll()?:""
    }

    val resourcePathName: String
        get() {
            val resourcePathName = resource.resourcePathName

            return if (resourcePathName.isEmpty()) {
                resource.displayName.value.toLowerCase()
            } else resourcePathName
        }

    open fun getExample(): String? {
        val s = method.bodies?.
                getOrNull(0)?.
                type?.
                examples?.
                getOrNull(0)?.
                value
        return StringEscapeUtils.escapeJson(s?.toJson())
    }

    fun singularName(): String {
        return resource.resourcePathName.singularize()
    }
}