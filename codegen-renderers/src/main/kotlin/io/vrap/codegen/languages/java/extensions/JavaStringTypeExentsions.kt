package io.vrap.codegen.languages.java.extensions

import io.vrap.rmf.raml.model.util.StringCaseFormat

/**
 * Returns this string as standard java enum name (upper underscore case).
 */
fun String.enumValueName(): String {
    return StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(this)
}
