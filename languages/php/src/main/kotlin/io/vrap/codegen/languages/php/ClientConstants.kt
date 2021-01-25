package io.vrap.codegen.languages.php

import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.di.SharedPackageName

class ClientConstants constructor(
        @SharedPackageName
        val sharedPackageName: String,
        @ClientPackageName
        val clientPackage: String,
        @BasePackageName
        val basePackagePrefix: String
) {
}
