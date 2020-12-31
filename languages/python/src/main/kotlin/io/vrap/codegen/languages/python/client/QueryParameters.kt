/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.client

import io.vrap.codegen.languages.python.snakeCase
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance

fun QueryParameter.template(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val template = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
        val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
        val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance

        val replace = template.value.replace("<" + placeholder.value + ">", "{k}")
        return "{f'$replace': v for k, v in ${paramName.value.snakeCase()}.items()}"
    }
    return "'" + this.name + "'"
}

fun QueryParameter.paramName(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val paramName = o.value.stream()
            .filter { propertyValue -> propertyValue.name == "paramName" }
            .findFirst()
            .orElse(null).value as StringInstance
        return paramName.value.snakeCase()
    }
    return this.name.snakeCase()
}
