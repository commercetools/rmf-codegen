package io.vrap.codegen.languages.typescript.model

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.google.common.collect.Lists
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.util.StringCaseFormat

fun VrapType.simpleTSName():String{
    return when(this){
        is VrapAnyType -> this.baseType
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "${this.itemType.simpleTSName()}[]"
        is VrapNilType -> this.name
    }
}
fun Resource.resourcePathList(): List<Resource> {
    val path = Lists.newArrayList<Resource>()
    if (this.fullUri.template == "/") {
        return path
    }
    path.add(this)
    var t = this.eContainer()
    while (t is Resource) {
        val template = t.fullUri.template
        if (template != "/") {
            path.add(t)
        }
        t = t.eContainer()
    }
    return Lists.reverse(path)
}
fun Method.firstBody(): Body? = this.bodies.stream().findFirst().orElse(null)

fun UriTemplate.paramValues(): List<String> {
    return this.components.filterIsInstance<Expression>().flatMap { expression -> expression.varSpecs.map { varSpec -> varSpec.variableName  } }
}
fun QueryParameter.methodName(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
        return "with" + StringCaseFormat.UPPER_CAMEL_CASE.apply(paramName.value)
    }
    return "with" + StringCaseFormat.UPPER_CAMEL_CASE.apply(this.name.replace(".", "-"))
}

