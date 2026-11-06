package io.vrap.codegen.languages.rust

import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapScalarType

/**
 * Maps the RAML built-in types to Rust. The date types point at a `support`
 * module at the crate root, which holds the hand-written date wrappers.
 */
object RustBaseTypes : LanguageBaseTypes(
    anyType = nativeRustType("serde_json::Value"),
    objectType = nativeRustType("serde_json::Value"),
    integerType = nativeRustType("i64"),
    longType = nativeRustType("i64"),
    doubleType = nativeRustType("f64"),
    stringType = nativeRustType("String"),
    booleanType = nativeRustType("bool"),
    dateTimeType = nativeRustType("crate::support::DateTime"),
    dateOnlyType = nativeRustType("crate::support::Date"),
    timeOnlyType = nativeRustType("crate::support::Time"),
    file = nativeRustType("Vec<u8>")
)

fun nativeRustType(typeName: String): VrapScalarType = VrapScalarType(typeName)
