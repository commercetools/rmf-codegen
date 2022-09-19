package io.vrap.codegen.languages.csharp.extensions

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.google.common.collect.Lists
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import com.hypertino.inflector.English
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.types.*
import java.util.*

fun VrapType.simpleName(): String {
    return when (this) {
        is VrapScalarType -> this.scalarType
        is VrapDateTimeType -> this.simpleClassName
        is VrapEnumType -> "I"+this.simpleClassName
        is VrapObjectType -> if(this.simpleClassName == "Date" || this.simpleClassName == "DateTime" || this.simpleClassName == "TimeSpan" || this.simpleClassName == "Object") this.simpleClassName else "I${this.simpleClassName}"
        is VrapAnyType -> this.baseType
        is VrapArrayType -> """List\<${this.itemType.simpleName()}\>"""
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.toCsharpVType(): VrapType {
    return when (this) {
        is VrapObjectType -> {
            VrapObjectType(`package` = this.`package`.toCsharpPackage(), simpleClassName = this.simpleClassName)
        }
        is VrapEnumType -> {
            VrapEnumType(`package` = this.`package`.toCsharpPackage(), simpleClassName = this.simpleClassName)
        }
        is VrapArrayType -> {
            VrapArrayType(this.itemType.toCsharpVType())
        }
        else -> {
            this
        }

    }
}

fun VrapType.csharpPackage(): String {
    var packageName = ""
    if (this is VrapObjectType) packageName = this.`package`
    else if (this is VrapEnumType) packageName = this.`package`

    if(!packageName.isNullOrEmpty())
        packageName = packageName.toCsharpPackage()
    return packageName
}
//need refactor
fun VrapType.requestBuildersPackage(resourceNamePlural: String): String {
    var cPackage = this.csharpPackage().replace("Clients","Client")
    return if(resourceNamePlural.isNullOrEmpty()) "$cPackage.RequestBuilders" else "$cPackage.RequestBuilders.$resourceNamePlural"
}

/**
 * Returns package "commercetools.API/models/Order" as csharp namespace, example "commercetools.API.Models.Orders"
 * Don't appply camelCase to commercetools
 */
fun String.toCsharpPackage():String{
    if(!this.contains("/"))
        return this
    var packageAsList = this.split("/")
    var first = packageAsList.first()
    var domainTypeAsPlural = packageAsList.last().singularize().pluralize()
    packageAsList = packageAsList.dropLast(1).plus(domainTypeAsPlural)

    return packageAsList.takeLast(maxOf(packageAsList.size, 1)).joinToString(".")
    {
        s -> if(s == first) {
        return@joinToString s
        } else return@joinToString StringCaseFormat.UPPER_CAMEL_CASE.apply(s)
    }
}

/**
 * Returns physical path of the file should be generated like "commercetools/API/models/Orders/OrderDraft.cs"
 */
fun VrapType.csharpClassRelativePath(isInterface: Boolean = false): String {
    var packageName = "";
    var simpleClassName = ""

    if (this is VrapObjectType) {
        packageName = this.`package`
        simpleClassName = this.simpleClassName
    } else if (this is VrapEnumType) {
        packageName = this.`package`
        simpleClassName = this.simpleClassName
    }

    var namespaceDir = packageName.toNamespaceDir()
    var fileName = if (isInterface) "I${simpleClassName}" else "${simpleClassName}"

    return "${namespaceDir}.${fileName}".replace(".", "/") + ".cs"
}

/**
 * Package example "commercetools.API/models/Order"
 * Returns physical path of the file should be generated like "commercetools/API/Models/Orders"
 */
fun String.toNamespaceDir():String{
    if(!this.contains("/"))
        return this

    var packageAsList = this.split("/")
    var domainTypeAsPlural = packageAsList.last().singularize().pluralize()
    packageAsList = packageAsList.dropLast(1).plus(domainTypeAsPlural)
    return packageAsList.joinToString("/") { s -> StringCaseFormat.UPPER_CAMEL_CASE.apply(s) }.lowerCamelCase()
}

fun String.pluralize(): String {
    val typesToExcluded = arrayOf("common", "me", "graphql")
    if(typesToExcluded.contains(this.lowercase(Locale.getDefault())))
        return this
   return English.plural(this)
}

fun String.singularize(): String {
    return English.singular(this)
}

fun AnyType.isNullableScalar(): Boolean {
    return when (this) {
        is IntegerType -> true
        is NumberType -> true
        is BooleanType -> true
        is DateTimeType -> true
        is DateOnlyType -> true
        is DateTimeOnlyType -> true
        is TimeOnlyType -> true
        is StringType -> false
        is ArrayType -> false
        else -> false
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
fun UriTemplate.paramValues(): List<String> {
    return this.components.filterIsInstance<Expression>().flatMap { expression -> expression.varSpecs.map { varSpec -> varSpec.variableName  } }
}

fun Method.firstBody(): Body? = this.bodies.stream().findFirst().orElse(null)

fun QueryParameter.methodName(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
        return "With" + StringCaseFormat.UPPER_CAMEL_CASE.apply(paramName.value)
    }
    return "With" + StringCaseFormat.UPPER_CAMEL_CASE.apply(this.name.replace(".", "-"))
}
