package io.vrap.codegen.languages.rust

import io.vrap.codegen.languages.rust.model.RustTypesFileProducer
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.types.PackageProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.nio.file.Paths

class RustTypesTest {

    private val files: Map<String, String> by lazy { generate() }

    private fun generate(): Map<String, String> {
        val apiPath = Paths.get(javaClass.getResource("/rust/api.raml")!!.toURI())
        val generatorModule = RamlGeneratorModule(
            RamlApiProvider(apiPath),
            CodeGeneratorConfig(basePackageName = ""),
            RustBaseTypes
        )
        val producer = RustTypesFileProducer(
            generatorModule.vrapTypeProvider(),
            PackageProvider(
                generatorModule.providePackageName(),
                generatorModule.provideModelPackageName(),
                generatorModule.provideClientPackageName()
            ),
            generatorModule.allAnyTypes()
        )
        return producer.produceFiles().associate { it.relativePath to it.content }
    }

    private fun cart(): String = files.getValue("cart.rs")

    private fun assertContains(text: String, expected: String) {
        assertTrue(text.contains(expected)) { "expected to find:\n$expected\nin:\n$text" }
    }

    @Test
    fun writesOneFilePerPackageAndADeclaringModule() {
        assertEquals(setOf("cart.rs", "common.rs", "mod.rs"), files.keys)
        assertContains(files.getValue("mod.rs"), "pub mod cart;\npub mod common;")
    }

    @Test
    fun writesStringEnumsWithAnUnknownCaseAndAStrictDecoder() {
        val common = files.getValue("common.rs")
        assertContains(common, "pub enum ReferenceTypeId {\n    Cart,\n    CustomerGroup,\n    Self_,\n    Unknown(String),\n}")
        assertContains(common, "\"customer-group\" => Self::CustomerGroup,")
        assertContains(common, "_ => Self::Unknown(value),")
    }

    @Test
    fun writesAliasesForNamedScalarsAndArrays() {
        assertContains(files.getValue("common.rs"), "pub type CountryCode = String;")
        assertContains(files.getValue("common.rs"), "pub type CountryCodes = Vec<String>;")
    }

    @Test
    fun writesMapsAsBTreeMapAliases() {
        assertContains(files.getValue("common.rs"), "pub type LocalizedString = std::collections::BTreeMap<String, String>;")
    }

    @Test
    fun writesDiscriminatedUnionsWithATagFirstDecoder() {
        val cart = cart()
        assertContains(cart, "#[serde(untagged)]\n#[non_exhaustive]\npub enum CartUpdateAction {\n    AddLineItem(Box<CartAddLineItemAction>),\n    SetKey(Box<CartSetKeyAction>),\n    Unknown(serde_json::Value),\n}")
        assertContains(cart, ".get(\"action\")")
        assertContains(cart, "\"addLineItem\" => serde_json::from_value::<CartAddLineItemAction>(value)")
        assertContains(cart, "impl From<CartAddLineItemAction> for CartUpdateAction {")
    }

    @Test
    fun variantStructsOwnTheirTagAndLeaveItOutOfTheirFields() {
        val cart = cart()
        assertContains(cart, "#[serde(tag = \"action\", rename = \"addLineItem\")]\n#[non_exhaustive]\npub struct CartAddLineItemAction {")
        val variant = cart.substringAfter("pub struct CartAddLineItemAction {").substringBefore("}")
        assertTrue(!variant.contains("pub action")) { "the tag must not be a field: $variant" }
    }

    @Test
    fun writesReferencesToOtherModulesThroughTheirModulePath() {
        assertContains(cart(), "pub customer_group: Option<super::common::CustomerGroupReference>,")
        assertContains(cart(), "pub reference: Option<super::common::Reference>,")
    }

    @Test
    fun writesInlineUnionsAsEnumsNamedAfterTheirOwnerAndField() {
        val cart = cart()
        assertContains(cart, "pub enum CartTarget {\n    Cart(Box<CartReference>),\n    CustomerGroup(Box<super::common::CustomerGroupReference>),\n    Unknown(serde_json::Value),\n}")
        assertContains(cart, "pub target: CartTarget,")
    }

    @Test
    fun boxesFieldsThatCloseAContainmentCycleOnly() {
        val cart = cart()
        assertContains(cart, "pub next: Option<Box<Node>>,")
        assertContains(cart, "pub other: Box<Other>,")
        assertContains(cart, "pub pair: Option<Box<Pair>>,")
        assertContains(cart, "pub items: Vec<Pair>,")
    }

    @Test
    fun keepsRequiredAndOptionalFieldsAndAddsRenamesOnlyWhenNeeded() {
        val cart = cart()
        assertContains(cart, "pub id: String,")
        assertContains(cart, "pub version: i64,")
        assertContains(cart, "#[serde(rename = \"type\")]\n    pub type_: Option<String>,")
        assertContains(cart, "#[serde(rename = \"ISOCode\")]\n    pub isocode: Option<String>,")
        assertContains(cart, "pub is_platform_client: Option<bool>,")
        assertContains(cart, "pub created_at: crate::support::DateTime,")
    }

    @Test
    fun writesPatternPropertiesAsAFlattenedExtraMap() {
        assertContains(files.getValue("common.rs"), "#[serde(flatten)]\n    pub extra: std::collections::BTreeMap<String, String>,")
    }

    @Test
    fun treatsObjFieldsAsOpaqueJson() {
        assertContains(cart(), "pub obj: Option<serde_json::Value>,")
    }

    @Test
    fun keepsTheUnknownKeysOfAnObjectWithoutProperties() {
        assertContains(
            cart(),
            "pub struct OpenObject {\n    #[serde(flatten)]\n    pub extra: std::collections::BTreeMap<String, serde_json::Value>,\n}"
        )
    }

    @Test
    fun declaresTheTagOfAVariantWithPatternPropertiesAsASkippedField() {
        // The flattened map would take the tag too, so the tag gets its own field, which is never written.
        val common = files.getValue("common.rs")
        assertContains(common, "#[serde(tag = \"code\", rename = \"InvalidInput\")]\n#[non_exhaustive]\npub struct InvalidInputError {\n    #[serde(rename = \"code\", skip_serializing)]\n    pub code: Option<String>,")
        assertContains(common, "    #[serde(flatten)]\n    pub extra: std::collections::BTreeMap<String, String>,")
    }

    @Test
    fun declaresTheImportsEachModuleUses() {
        assertContains(cart(), "use serde::{Deserialize, Deserializer, Serialize};\nuse serde::de::Error as _;")
        assertContains(files.getValue("common.rs"), "use serde::{Deserialize, Deserializer, Serialize, Serializer};")
    }
}
