package io.vrap.codegen.languages.php

import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapNilType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.types.BooleanInstance
import io.vrap.rmf.raml.model.types.FileType

abstract class AbstractRequestBuilder constructor(
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider,
        clientConstants: ClientConstants
) : EObjectTypeExtensions {

    protected val basePackagePrefix = clientConstants.basePackagePrefix

    protected val sharedPackageName = clientConstants.sharedPackageName

    protected val clientPackageName = clientConstants.clientPackage

    protected fun Resource.methods(): String {
        return this.methods.joinToString(separator = "") {
            """
                |/**
                | * @psalm-param ${it.bodyType() ?: "?object|array|string"} $!body
                | * @psalm-param array<string, scalar|scalar[]> $!headers ${if (it.markDeprecated()) """
                | * @deprecated""" else ""}
                | */
                |public function ${it.methodName}(${it.bodyType()?.plus(" ") ?: ""}$!body = null, array $!headers = []): ${it.toRequestName()}
                |{
                |    $!args = $!this->getArgs();
                |
                |    return new ${it.toRequestName()}(${it.allParams()?.joinToString("") { p -> "$!args['$p'], " }}$!body, $!headers, $!this->getClient());
                |}
                |
            """.trimMargin()
        }
    }

    protected fun ResourceContainer.subResources(): String {
        return this.resources.filterNot { it.deprecated() }.joinToString(separator = "") {
            """
                |/**${if (it.markDeprecated()) """
                | * @deprecated""" else ""}
                | */
                |public function ${it.getMethodName()}(${it.relativeUri.paramValues().joinToString(", ") { "string $$it = null" }}): ${it.resourceBuilderName()}
                |{
                |    $!args = $!this->getArgs();${it.relativeUri.paramValues().joinToString("\n") {"""
                |    if (!is_null($$it)) {
                |        $!args['$it'] = $$it;
                |    }""" }}
                |
                |    return new ${it.resourceBuilderName()}($!args, $!this->getClient());
                |}
                |
            """.trimMargin()
        }
    }

    protected fun Resource.deprecated() : Boolean {
        val anno = this.getAnnotation("deprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    protected fun Resource.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    protected fun Method.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    protected fun rootResource() = basePackagePrefix.replace(sharedPackageName, "").trim('/').toNamespaceName() + "RequestBuilder"

    protected fun Resource.resourceBuilderName():String = "Resource${this.toResourceName()}"
    private fun Resource.resourceBuilderFullName():String = "${clientPackageName.toNamespaceName()}\\Resource\\Resource${this.toResourceName()}"
    private fun Method.fullClassName():String = "${clientPackageName.toNamespaceName()}\\Resource\\${this.toRequestName()}"

    private fun Resource.methodReturnTypeImports() = this.methods.asSequence().mapNotNull { it.firstBody()?.type }
            .map { it.toVrapType() }
            .filter { !it.isScalar() }
            .map {
                when (it) {
                    is VrapObjectType -> it.fullClassName()
                    is VrapArrayType -> it.fullClassName()
                    else -> ""
                }
            }
            .filter { it != "" }
            .map { "use ${it.escapeAll()};" }

    protected fun Resource.imports() = this.methodReturnTypeImports()
            .distinct()
            .sorted()
            .joinToString("\n")

    protected fun Api.imports(): String {
        val rootResource = if (this.resources.size == 1 && this.resources[0].resourcePath == "/") this.resources[0] else null
        val methodImports = rootResource?.methods?.asSequence()?.map { method -> "use ${method.fullClassName().escapeAll()};" } ?: emptySequence()
        return (rootResource?.resources ?: this.resources).asSequence()
                .map { "use ${it.resourceBuilderFullName().escapeAll()};" }
                .plus(methodImports)
                .plus(rootResource?.methodReturnTypeImports() ?: emptySequence())
                .distinct()
                .sorted()
                .joinToString("\n")
    }

    private fun Method.bodyType(): String? {
        val firstBody = this.firstBody()?.type
        val vrapType = firstBody.toVrapType()
        if (firstBody is FileType)
            return "?UploadedFileInterface "
        if (vrapType is VrapNilType || vrapType.simpleName() == "stdClass")
            return null
        return "?${vrapType.simpleName()}"
    }

//    protected fun Response.isSuccessful(): Boolean = this.statusCode.toInt() in (200..299)
//
//    protected fun Method.returnType(): AnyType {
//        return this.responses
//                .filter { it.isSuccessful() }
//                .filter { it.bodies?.isNotEmpty() ?: false }
//                .firstOrNull()
//                ?.let { it.bodies[0].type }
//                ?: TypesFactoryImpl.eINSTANCE.createNilType()
//    }
//
//    protected fun Method.returnTypeClass(): String {
//        val vrapType = this.returnType().toVrapType()
//        return when (vrapType) {
//            is VrapObjectType -> vrapType.simpleName()
//            else -> "JsonObject"
//        }
//    }
//
//    protected fun Method.returnTypeFullClass(): String {
//        val vrapType = this.returnType().toVrapType()
//        return when (vrapType) {
//            is VrapObjectType -> vrapType.fullClassName()
//            else -> "${sharedPackageName.toNamespaceName()}\\Base\\JsonObject"
//        }
//    }
}
