package io.vrap.codegen.languages.typescript.server

import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.types.VrapObjectType
import javax.inject.Inject

class ConstantsProvider @Inject constructor(@ClientPackageName val client_package: String){


    val parametersModule = "$client_package/parameters"

    val commonModule = "$client_package/common"

    val HttpResponse = VrapObjectType(
            commonModule,
            "HttpInput"
    )

    val HttpInput = VrapObjectType(
            commonModule,
            "HttpResponse"
    )

    val Resource = VrapObjectType(
            commonModule,
            "Resource"
    )

    val VariableMap = VrapObjectType(
            commonModule,
            "VariableMap"
    )
}


