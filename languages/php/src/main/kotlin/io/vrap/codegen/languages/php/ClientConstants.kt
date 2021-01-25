package io.vrap.codegen.languages.php

import com.google.inject.Inject
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.di.SharedPackageName
import io.vrap.rmf.raml.model.util.StringCaseFormat

class ClientConstants @Inject constructor(
        @SharedPackageName
        val sharedPackageName: String,
        @ClientPackageName
        val clientPackage: String,
        @BasePackageName
        val basePackagePrefix: String
) {
}
