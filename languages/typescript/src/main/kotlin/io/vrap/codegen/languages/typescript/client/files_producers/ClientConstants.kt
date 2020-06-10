package io.vrap.codegen.languages.typescript.client.files_producers

import com.google.inject.Inject
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.di.SharedPackageName
import io.vrap.rmf.raml.model.util.StringCaseFormat

class ClientConstants @Inject constructor(
        @SharedPackageName
        val sharedPackage: String,
        @ClientPackageName
        val clientPackage: String,
        @BasePackageName
        val basePackageName: String
) {

    val requestUtilsPackage = "${sharedPackage.lowerCasePackage()}/utils/requests-utils"
    val uriUtilsPackage = "${sharedPackage.lowerCasePackage()}/utils/uri-utils"
    val commonTypesPackage = "${sharedPackage.lowerCasePackage()}/utils/common-types"
    val middlewarePackage = "${sharedPackage.lowerCasePackage()}/utils/middleware"
    val apiRoot = "${clientPackage.lowerCasePackage()}/api-root"
    val indexFile = "index"
}

private fun String.lowerCasePackage(): String {
    return this.split("/").map { StringCaseFormat.LOWER_HYPHEN_CASE.apply(it) }.joinToString(separator = "/")
}