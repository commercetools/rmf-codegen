package io.vrap.codegen.languages.postman.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.Instance
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringInstance
import org.apache.commons.lang3.StringEscapeUtils
import java.io.IOException
import java.util.*
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction1

class ActionGenModel(val type: ObjectType, resource: Resource, template: KFunction1<ItemGenModel, String>, method: Method, renameParam: Function2<ItemGenModel, String, String> = { item, name -> name }) : ItemGenModel(resource, template, method, renameParam) {
    val testScript: String?
    private val example: String?
    val discriminatorValue: String
        get() = type.discriminatorValue

    override val description: String
        get() {
            val description = Optional.ofNullable(type.description).map<String> { it.value }.orElse(type.name)
            return StringEscapeUtils.escapeJson(description)
        }

    init {
        var example: String? = null
        var instance: Instance? = null

        if (type.getAnnotation("postman-example") != null) {
            instance = type.getAnnotation("postman-example").value
        } else if (type.examples.size > 0) {
            instance = type.examples[0].value
        }

        if (instance != null) {
            example = instance.toJson()
            try {
                val mapper = ObjectMapper()
                val nodes = mapper.readTree(example) as ObjectNode
                nodes.put("action", type.discriminatorValue)

                example = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodes)
                        .split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().map { s -> "  $s" }
                        .joinToString("\n")
                        .trim { it <= ' ' }
            } catch (e: IOException) {
            }

        }

        this.example = example
        val t = type.getAnnotation("postman-test-script")
        if (t != null) {
            this.testScript = (t.value as StringInstance).value
        } else {
            this.testScript = null
        }
    }

    override fun getExample(): String? {
        return example
    }
}