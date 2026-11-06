package io.vrap.codegen.languages.rust

/** Keywords that cannot be used as identifiers. `Self` is included because it is a keyword too. */
private val RESERVED = setOf(
    "as", "async", "await", "abstract", "become", "box", "break", "const", "continue", "crate",
    "do", "dyn", "else", "enum", "extern", "false", "final", "fn", "for", "gen", "if", "impl", "in",
    "let", "loop", "macro", "match", "mod", "move", "mut", "override", "priv", "pub", "ref", "return",
    "self", "Self", "static", "struct", "super", "trait", "true", "try", "type", "typeof", "unsafe",
    "unsized", "use", "virtual", "where", "while", "yield"
)

/**
 * Appends `_` to a reserved word, so `type` becomes `type_`.
 */
fun String.rustIdent(): String = if (this in RESERVED) "${this}_" else this

/**
 * Converts a RAML name such as `customerGroup` to snake case, `customer_group`.
 * An uppercase letter starts a new word only after a lowercase letter or a digit,
 * so `ISOCode` becomes `isocode`. The name must be checked with a serde rename afterwards.
 */
fun snakeCase(name: String): String {
    val builder = StringBuilder()
    name.forEachIndexed { index, char ->
        when {
            char.isUpperCase() -> {
                if (index > 0 && (name[index - 1].isLowerCase() || name[index - 1].isDigit())) {
                    builder.append('_')
                }
                builder.append(char.lowercaseChar())
            }
            char.isLetterOrDigit() -> builder.append(char)
            else -> builder.append('_')
        }
    }
    return builder.toString().replace(Regex("_+"), "_").trim('_')
}

/**
 * Converts a snake case field name back to the camel case that serde's
 * `rename_all = "camelCase"` produces. Used to decide whether a field needs an explicit rename.
 */
fun camelCase(snake: String): String =
    snake.split('_').filter { it.isNotEmpty() }.mapIndexed { index, part ->
        if (index == 0) part else part.replaceFirstChar { it.uppercaseChar() }
    }.joinToString("")

/**
 * Turns an enum value such as `customer-group` into a variant name such as `CustomerGroup`.
 * Values that start with a digit get a `V` prefix.
 */
fun pascalCase(value: String): String {
    val joined = value.split(Regex("[^A-Za-z0-9]+"))
        .filter { it.isNotEmpty() }
        .joinToString("") { it.replaceFirstChar { c -> c.uppercaseChar() } }
    val prefixed = if (joined.isEmpty() || joined.first().isDigit()) "V$joined" else joined
    return if (prefixed == "Self") "Self_" else prefixed
}

/**
 * The Rust module for a package. The package comes from the `(package)` annotation,
 * for example `models/Cart`, and the module is its last segment in snake case, `cart`.
 */
fun moduleNameOf(packageName: String): String = snakeCase(packageName.substringAfterLast('/')).rustIdent()

private val IDENT = Regex("[A-Za-z_][A-Za-z0-9_]*")

/** Fails loudly on a name that cannot become a Rust identifier, instead of writing broken code. */
fun requireIdent(name: String, what: String): String {
    require(IDENT.matches(name)) { "$what '$name' is not a valid Rust identifier" }
    return name
}

/** Escapes a value for a Rust string literal. */
fun String.escapeRust(): String = replace("\\", "\\\\").replace("\"", "\\\"")
