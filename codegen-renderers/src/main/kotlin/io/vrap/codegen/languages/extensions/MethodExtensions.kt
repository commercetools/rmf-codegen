package io.vrap.codegen.languages.extensions

import io.vrap.codegen.languages.php.extensions.toParamName
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.Response
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.impl.TypesFactoryImpl

fun Method.toRequestName(): String {
    return this.resource().fullUri.toParamName("By") + this.method.toString().capitalize()
}

fun Method.resource(): Resource = this.eContainer() as Resource


fun Method.returnType(): AnyType {
    return this.responses
            .filter { it.isSuccessfull() }
            .filter { it.bodies?.isNotEmpty() ?: false }
            .firstOrNull()
            ?.let { it.bodies[0].type }
            ?: TypesFactoryImpl.eINSTANCE.createNilType()
}

fun Response.isSuccessfull(): Boolean = this.statusCode.toInt() in (200..299)


