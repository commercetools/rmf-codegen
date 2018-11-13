package io.vrap.codegen.kt.languages.java.extensions

fun String.forcedLiteralEscape() = replace("$!", "$")
