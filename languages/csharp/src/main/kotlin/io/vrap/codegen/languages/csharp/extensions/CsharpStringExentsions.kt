package io.vrap.codegen.languages.csharp.extensions

import io.vrap.rmf.raml.model.util.StringCaseFormat

/**
 * Returns this string as standard C# enum name.
 */
fun String.enumValueName(): String {
    return StringCaseFormat.UPPER_CAMEL_CASE.apply(this)
}
