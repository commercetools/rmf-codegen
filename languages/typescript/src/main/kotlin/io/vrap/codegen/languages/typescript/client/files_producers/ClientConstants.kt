package io.vrap.codegen.languages.typescript.client.files_producers

import com.google.inject.Inject
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.di.SharedPackageName

class ClientConstants @Inject constructor(
        @SharedPackageName
        val sharedPackage: String,
        @ClientPackageName
        val clientPackage: String,
        @BasePackageName
        val basePackageName: String
) {

    val requestUtilsPackage = "${sharedPackage}/utils/requests-utils"
    val uriUtilsPackage = "${sharedPackage}/utils/uri-utils"
    val commonTypesPackage = "${sharedPackage}/utils/common-types"
    val middlewarePackage = "${sharedPackage}/utils/middleware"
    val apiRoot = "$clientPackage/api-root"
    val indexFile = "index"
}