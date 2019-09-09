package io.vrap.codegen.languages.php.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.di.SharedPackageName
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapNilType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.responses.Response
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.FileType
import io.vrap.rmf.raml.model.types.impl.TypesFactoryImpl

abstract class AbstractRequestBuilder constructor(
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider
) : EObjectTypeExtensions {

    @Inject
    @BasePackageName
    lateinit var basePackagePrefix: String

    @Inject
    @SharedPackageName
    lateinit var sharedPackageName: String


    @Inject
    @ClientPackageName
    lateinit var clientPackageName: String

    protected fun Resource.methods(): String {
        return this.methods.map {
            """
                |/**
                | * @psalm-param ${it.bodyType() ?: "?object "}$!body
                | * @psalm-param array<string, scalar|scalar[]> $!headers
                | */
                |public function ${it.methodName}(${it.bodyType() ?: ""}$!body = null, array $!headers = []): ${it.toRequestName()} {
                |   $!args = $!this->getArgs();
                |   return new ${it.toRequestName()}(${it.allParams()?.map { "$!args['${it}'], " }?.joinToString("")}$!body, $!headers, $!this->getClient());
                |}
                |
            """.trimMargin()
        }.joinToString(separator = "")
    }

    protected fun ResourceContainer.subResources(): String {
        return this.resources.map {
            """
                |/**
                | <<${it.uriParameters?.asSequence()?.map { "* @psalm-param scalar $${it.name}" }?.joinToString(separator = "\n") ?: "*"}>>
                | */
                |public function ${it.getMethodName()}(${it.uriParameters?.asSequence()?.map { "$${it.name} = null"  }?.joinToString(", ") ?: ""}): ${it.resourceBuilderName()} {
                |   $!args = $!this->getArgs();
                |   ${it.uriParameters?.asSequence()?.map { "if (!is_null($${it.name})) { $!args['${it.name}'] = $${it.name}; }" }?.joinToString("\n")}
                |   return new ${it.resourceBuilderName()}($!this->getUri() . '${it.relativeUri.template}', $!args, $!this->getClient());
                |}
                |
            """.trimMargin()
        }.joinToString(separator = "")
    }


    protected fun Response.isSuccessfull(): Boolean = this.statusCode.toInt() in (200..299)

    protected fun Method.returnType(): AnyType {
        return this.responses
                .filter { it.isSuccessfull() }
                .filter { it.bodies?.isNotEmpty() ?: false }
                .firstOrNull()
                ?.let { it.bodies[0].type }
                ?: TypesFactoryImpl.eINSTANCE.createNilType()
    }

    protected fun Method.returnTypeClass(): String {
        val vrapType = this.returnType().toVrapType()
        return when (vrapType) {
            is VrapObjectType -> vrapType.simpleName()
            else -> "JsonObject"
        }
    }

    protected fun Method.returnTypeFullClass(): String {
        val vrapType = this.returnType().toVrapType()
        return when (vrapType) {
            is VrapObjectType -> vrapType.fullClassName()
            else -> "${sharedPackageName.toNamespaceName()}\\Base\\JsonObject"
        }
    }

    fun Resource.resourceBuilderName():String = "Resource${this.toResourceName()}"
    fun Resource.resourceBuilderFullName():String = "${clientPackageName.toNamespaceName()}\\Resource\\Resource${this.toResourceName()}"

    protected fun Resource.imports() = this.methods.asSequence().mapNotNull { it.firstBody()?.type }
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
            .distinct()
            .sorted()
            .joinToString("\n")

    protected fun Api.imports() = this.resources.asSequence()
            .map { "use ${it.resourceBuilderFullName().escapeAll()};" }
            .distinct()
            .sorted()
            .joinToString("\n")

    protected fun Method.bodyType(): String? {
        val firstBody = this.firstBody()?.type
        val vrapType = firstBody.toVrapType()
        if (firstBody is FileType)
            return "?UploadedFileInterface "
        if (vrapType is VrapNilType || vrapType.simpleName() == "stdClass")
            return null
        return "?${vrapType.simpleName()} "
    }
}
