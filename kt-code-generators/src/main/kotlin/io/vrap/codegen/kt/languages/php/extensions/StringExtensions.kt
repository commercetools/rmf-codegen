package io.vrap.codegen.kt.languages.php.extensions

fun String.forcedLiteralEscape() = replace("$!", "$")
