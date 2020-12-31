/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python

import io.vrap.rmf.raml.model.util.StringCaseFormat

fun String.toSchemaPackageName(): String {
    val parts = this.split(".")
    if (parts.size < 2) {
        println("[error] $this")
    }
    return listOf(parts[0], "_schemas", parts[1]).joinToString(separator = ".")
}

fun String.toRelativePackageName(base: String): String {
    val partsTo = this.split(".")
    val partsFrom = base.split(".")

    var path = ""
    for (i in partsFrom.size - 1 downTo 0) {
        if (i < partsTo.size) {
            if (partsFrom.slice(0..i).joinToString(".") == partsTo.slice(0..i).joinToString(".")) {
                if (path == "") path = "."
                return path + partsTo.slice(i + 1..partsTo.size - 1).joinToString(separator = ".")
            }
        }
        path += "."
    }
    return path + this
}

fun String.lowerCasePackage(): String {
    return this.split("/").map { StringCaseFormat.LOWER_UNDERSCORE_CASE.apply(it) }.joinToString(separator = ".")
}

fun String.snakeCase(): String {
    val name = StringCaseFormat.LOWER_UNDERSCORE_CASE.apply(this.replace(".", "_"))
    var keywords = arrayOf(
        "False", "None", "True", "and", "as", "assert", "async", "await",
        "break", "class", "continue", "def", "del", "elif", "else", "except",
        "finally", "for", "from", "global", "if", "import", "in", "is",
        "lambda", "nonlocal", "not", "or", "pass", "raise", "return", "try",
        "while", "with", "yield"
    )
    if (keywords.contains(name)) {
        return name + "_"
    }
    return name
}
