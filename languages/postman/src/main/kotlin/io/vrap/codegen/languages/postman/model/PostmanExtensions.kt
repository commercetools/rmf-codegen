package io.vrap.codegen.languages.postman.model

import com.hypertino.inflector.English
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.security.OAuth20Settings
import io.vrap.rmf.raml.model.types.StringInstance
import org.apache.commons.lang3.StringEscapeUtils
import org.eclipse.emf.ecore.EObject
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

fun String.singularize(): String {
    return English.singular(this)
}

fun <T> EObject.getParent(parentClass: Class<T>): T? {
    if (this.eContainer() == null) {
        return null
    }
    return if (parentClass.isInstance(this.eContainer())) {
        this.eContainer() as T
    } else this.eContainer().getParent(parentClass)
}

fun StringInstance.description(): String {
    return this.value.escapeJson()?.escapeAll()?:""
}
