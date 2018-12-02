package io.vrap.codegen.languages.php.extensions

fun String.forcedLiteralEscape() = replace("$!", "$")
