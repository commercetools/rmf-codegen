package io.vrap.codegen.languages.postman.model

import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.security.OAuth20Settings
import org.apache.commons.lang3.StringEscapeUtils
import java.net.URI


fun String.escapeJson(): String {
    return StringEscapeUtils.escapeJson(this)
}

fun OAuth20Settings.uri(): URI {
    return URI.create(this.accessTokenUri)
}

fun Api.oAuth2(): OAuth20Settings {
    return this.securitySchemes.stream()
            .filter { securityScheme -> securityScheme.settings is OAuth20Settings }
            .map { securityScheme -> securityScheme.settings as OAuth20Settings }
            .findFirst().orElse(null)
}