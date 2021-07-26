package io.vrap.codegen.languages.jsonschema.model

fun create_schema_id (identifier: String): String  {
    return "https://api.commercetools.com/${identifier}"
}
