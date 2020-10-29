package io.vrap.codegen.languages.csharp.extensions

import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import com.hypertino.inflector.English

fun VrapType.simpleName(): String {
    return when (this) {
        is VrapScalarType -> this.scalarType
        is VrapDateTimeType -> this.simpleClassName
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> if(this.simpleClassName == "DateTime" || this.simpleClassName == "Object") this.simpleClassName else "I${this.simpleClassName}"
        is VrapAnyType -> this.baseType
        is VrapArrayType -> """List\<${this.itemType.simpleName()}\>"""
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.") as Throwable
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
    var cPackage = this.csharpPackage()
    return cPackage.replace("Clientss","Client").replace("Clients","Client")+".RequestBuilders"+"."+resourceNamePlural
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
    var relativePath = "";
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
    var fileName = if(isInterface) "I${simpleClassName}" else simpleClassName

    relativePath = "${namespaceDir}.${fileName}".replace(".", "/") + ".cs"

    return relativePath
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
    val typesToExcluded = arrayOf<String>("common", "me", "graphql")
    if(typesToExcluded.contains(this.toLowerCase()))
        return this
   return return English.plural(this)
}

fun String.singularize(): String {
    return English.singular(this)
}

fun VrapType.isValueType(): Boolean {
    return (this is VrapScalarType) && this.scalarType!="string"
}
