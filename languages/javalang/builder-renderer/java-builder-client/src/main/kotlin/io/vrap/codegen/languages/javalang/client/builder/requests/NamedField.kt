package io.vrap.codegen.languages.javalang.client.builder.requests

import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType

data class NamedField(val type: VrapType, val name: String)

val apiHttpClient = VrapObjectType(`package` = "client.ApiHttpClient",simpleClassName = "ApiHttpClient")