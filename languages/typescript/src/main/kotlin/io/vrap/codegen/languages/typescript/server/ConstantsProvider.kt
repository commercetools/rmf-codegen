package io.vrap.codegen.languages.typescript.server

import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.types.VrapLibraryType
import io.vrap.rmf.codegen.types.VrapObjectType

class ConstantsProvider constructor(@ClientPackageName val client_package: String){

    val parametersModule = "$client_package/parameters"

    val serverModule = "$client_package/server"

    val commonModule = "$client_package/common"

    val ErrorHandler = VrapObjectType(
            commonModule,
            "ErrorHandler"
    )

    val Resource = VrapObjectType(
            commonModule,
            "Resource"
    )

    val ScalarValue = VrapObjectType(
            commonModule,
            "ScalarValue"
    )

    val ServerRoute = VrapLibraryType(
            "@hapi/hapi",
            "ServerRoute"
    )

    val Lifecycle = VrapLibraryType(
            "@hapi/hapi",
            "Lifecycle"
    )

    val ResponseToolkit = VrapLibraryType(
            "@hapi/hapi",
            "ResponseToolkit"
    )

    val Request = VrapLibraryType(
            "@hapi/hapi",
            "Request"
    )
}
