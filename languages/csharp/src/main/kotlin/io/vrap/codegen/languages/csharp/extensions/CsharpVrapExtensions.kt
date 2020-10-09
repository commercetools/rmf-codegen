package io.vrap.codegen.languages.csharp.extensions

import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat

fun VrapType.simpleName(): String {
    return when (this) {
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapAnyType -> this.baseType
        is VrapArrayType -> """List\<${this.itemType.simpleName()}\>"""
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.fullClassName(): String {
    return when (this) {
        is VrapAnyType -> this.baseType
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> "${this.`package`}.${this.simpleClassName}"
        is VrapObjectType -> "${this.`package`}.${this.simpleClassName}"
        is VrapArrayType -> """"System.Collections.Generic.List\<${this.itemType.fullClassName()}\>"""
        is VrapNilType -> "void"
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
    var domainTypeAsPlural = packageAsList.last().toPlural()
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
fun VrapType.csharpClassRelativePath(): String {
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

    relativePath = "${namespaceDir}.${simpleClassName}".replace(".", "/") + ".cs"

    return relativePath
}

/**
 * Package example "commercetools.API/models/Order"
 * Returns physical path of the file should be generated like "commercetools/API/models/Orders"
 */
fun String.toNamespaceDir():String{
    var packageAsList = this.split("/")
    var first = packageAsList.first()
    var domainTypeAsPlural = packageAsList.last().toPlural()
    packageAsList = packageAsList.dropLast(1).plus(domainTypeAsPlural)
    return packageAsList.takeLast(maxOf(packageAsList.size, 1)).joinToString("/") { s -> StringCaseFormat.UPPER_CAMEL_CASE.apply(s) }
}

fun String.toPlural(): String {
    val typesToExcluded = arrayOf<String>("common", "me", "graphql")
    if(typesToExcluded.contains(this.toLowerCase()))
        return this
    //need to plural it
   return this +"s"
}

//need to be implemented
fun VrapType.isValueType(): Boolean {
    return false
}