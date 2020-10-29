package io.vrap.codegen.languages.csharp.extensions

import io.vrap.rmf.raml.model.util.StringCaseFormat

/**
 * Returns this string as standard C# enum name.
 */
fun String.enumValueName(): String {
    return StringCaseFormat.UPPER_CAMEL_CASE.apply(this)
}
fun String.upperCamelCase() : String {
    return StringCaseFormat.UPPER_CAMEL_CASE.apply(this)
}

fun String.lowerCamelCase() : String {
    return StringCaseFormat.LOWER_CAMEL_CASE.apply(this)
}