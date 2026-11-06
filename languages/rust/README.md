# Rust generator

Generates Rust types for the RAML types of an API. It is the `RUST_TYPES` target of the CLI.

```
rmf-codegen generate -t RUST_TYPES -o <output-dir> <api.raml>
```

The output is one `<package>.rs` file per RAML `(package)`, plus a `mod.rs` that declares them. The files use `serde`, `serde_json` and `serde_with`. Date fields use `crate::support::{DateTime, Date, Time}`, which the crate that holds the generated files must provide.

## What is generated

| RAML | Rust |
| --- | --- |
| object | `struct`, with `#[serde_with::skip_serializing_none]` and `#[non_exhaustive]` |
| object with a `discriminatorValue` | `struct` that owns its tag through `#[serde(tag = ...)]` |
| discriminator base with variants | `enum`, serialized untagged and deserialized by its tag, with an `Unknown(serde_json::Value)` case |
| union of variants with one shared discriminator | the same kind of `enum`, named after its owner and field when inline |
| string enum | `enum` with an `Unknown(String)` case, so a new value still decodes |
| object with only pattern properties | `BTreeMap<String, T>` alias |
| object with no properties | `struct` with an `extra` map that keeps unknown keys |
| other unions, such as `string \| Money` | `serde_json::Value` |
| field that closes a containment cycle | `Box<T>` |

## Not yet

- Operations and request builders. The generator writes types only.
- Builders for the structs, and typed codes such as `CountryCode` (they are `String` for now).
- Typed inline enums: an enum written inline in a property is `String`.
- Documentation comments from the spec descriptions.
- Field, parameter and type overrides, such as the `obj` opaque field or `add_fields`. Only the `obj` field is treated as opaque, by name.
