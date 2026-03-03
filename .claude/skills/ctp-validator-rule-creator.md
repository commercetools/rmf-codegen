# CTP Validator Rule Creator

This skill guides creation of new validation rules for the ctp-validators Kotlin project, including implementation, tests, violation detection, documentation, and Confluence updates.

---

## End-to-End Workflow

### Step 1: Implement the Rule (rmf-codegen)

#### 1a. Create Rule Class

**Location:** `ctp-validators/src/main/kotlin/com/commercetools/rmf/validators/<RuleName>Rule.kt`

```kotlin
package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class <RuleName>Rule(severity: RuleSeverity, options: List<RuleOption>? = null) : TypesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }
            ?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    // Choose the appropriate case method based on what you're validating:
    // - caseObjectType(type: ObjectType) — for validating properties with access to parent type name
    // - caseProperty(property: Property) — for validating individual properties
    // - caseStringType(type: StringType) — for validating string/enum types
    // - caseBooleanType(type: BooleanType) — for boolean types
    // - caseArrayType(type: ArrayType) — for array types
    override fun caseObjectType(type: ObjectType): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        type.properties.forEach { property ->
            if (exclude.contains("${type.name}:${property.name}").not()) {
                // Your validation logic here
                if (/* condition */) {
                    validationResults.add(create(type, "Property \"{0}\" of type \"{1}\" <error description>", property.name, type.name))
                }
            }
        }
        return validationResults
    }

    companion object : ValidatorFactory<<RuleName>Rule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): <RuleName>Rule {
            return <RuleName>Rule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): <RuleName>Rule {
            return <RuleName>Rule(severity, options)
        }
    }
}
```

**Key decisions:**
- Use `@ValidatorSet` annotation for automatic discovery (included in default set)
- Use `create()` (not `error()`) so severity is configurable via ruleset XML
- Extend `TypesRule` for type/property validation, `ResourcesRule` for HTTP method/resource validation, `ModulesRule` for module-level validation
- Exclusion format: `TypeName:propertyName` for property rules, `TypeName` for type-level rules, plain `paramName` for query parameter/header rules

#### 1a-alt. ResourcesRule Template (for query parameter/header validation)

If validating query parameters or headers, extend `ResourcesRule` instead of `TypesRule`:

```kotlin
package com.commercetools.rmf.validators

import io.vrap.rmf.raml.model.resources.Method
import org.eclipse.emf.common.util.Diagnostic
import java.util.*

@ValidatorSet
class <RuleName>Rule(severity: RuleSeverity, options: List<RuleOption>? = null) : ResourcesRule(severity, options) {

    private val exclude: List<String> =
        (options?.filter { ruleOption -> ruleOption.type.lowercase(Locale.getDefault()) == RuleOptionType.EXCLUDE.toString() }
            ?.map { ruleOption -> ruleOption.value }?.plus("") ?: defaultExcludes)

    override fun caseMethod(method: Method): List<Diagnostic> {
        val validationResults: MutableList<Diagnostic> = ArrayList()

        method.queryParameters.forEach { queryParameter ->
            run {
                if (exclude.contains(queryParameter.name).not() && queryParameter.pattern == null) {
                    if (/* condition */) {
                        validationResults.add(create(queryParameter, "Query parameter \"{0}\" <error description>", queryParameter.name))
                    }
                }
            }
        }

        method.headers.forEach { header ->
            run {
                if (exclude.contains(header.name).not()) {
                    if (/* condition */) {
                        validationResults.add(create(header, "Header \"{0}\" <error description>", header.name))
                    }
                }
            }
        }

        return validationResults
    }

    companion object : ValidatorFactory<<RuleName>Rule> {
        private val defaultExcludes by lazy { listOf("") }

        @JvmStatic
        override fun create(options: List<RuleOption>?): <RuleName>Rule {
            return <RuleName>Rule(RuleSeverity.ERROR, options)
        }

        @JvmStatic
        override fun create(severity: RuleSeverity, options: List<RuleOption>?): <RuleName>Rule {
            return <RuleName>Rule(severity, options)
        }
    }
}
```

**Notes for ResourcesRule:**
- Use `ResourcesValidator` (not `TypesValidator`) in tests
- Access query params via `method.queryParameters`, headers via `method.headers`
- Skip pattern parameters with `queryParameter.pattern == null`
- Test RAML uses resource/method structure instead of `types:` block (see 1b-alt below)

#### 1b. Create Test RAML File

**Location:** `ctp-validators/src/test/resources/<rule-name-kebab-case>-rule.raml`

```raml
#%RAML 1.0
title: <rule name>

annotationTypes:
  package: string
  sdkBaseUri: string

baseUri: https://api.europe-west1.commercetools.com

types:
  Invalid<Name>:
    (package): Common
    type: object
    properties:
      badProperty:
        description: <why this is invalid>
        type: string

  Valid<Name>:
    (package): Common
    type: object
    properties:
      goodProperty:
        description: <why this is valid>
        type: string
```

#### 1b-alt. Test RAML for ResourcesRule (query parameters/headers)

```raml
#%RAML 1.0
title: <rule name>

/resources:
  get:
    queryParameters:
      invalidParam: string
      validParam: string
      excludedParam: string
    headers:
      invalidHeader: string
      validHeader: string
```

#### 1c. Add Test Methods

**Location:** `ctp-validators/src/test/groovy/com/commercetools/rmf/validators/ValidatorRulesTest.groovy`

```groovy
def "<rule name> rule"() {
    when:
    def options = singletonList(new RuleOption(RuleOptionType.EXCLUDE.toString(), "TypeName:excludedProperty"))
    def validators = Arrays.asList(new TypesValidator(Arrays.asList(<RuleName>Rule.create(options))))
    def uri = uriFromClasspath("/<rule-name-kebab-case>-rule.raml")
    def result = new RamlModelBuilder(validators).buildApi(uri)
    then:
    result.validationResults.size() == <N>
    result.validationResults[0].message == "<expected error message>"
}

def "<rule name> rule without exclusions"() {
    when:
    def validators = Arrays.asList(new TypesValidator(Arrays.asList(<RuleName>Rule.create(emptyList()))))
    def uri = uriFromClasspath("/<rule-name-kebab-case>-rule.raml")
    def result = new RamlModelBuilder(validators).buildApi(uri)
    then:
    result.validationResults.size() == <N+excluded>
}
```

**Validator classes by rule type:**
- `TypesValidator` — for rules extending `TypesRule`
- `ResourcesValidator` — for rules extending `ResourcesRule`
- `ModulesValidator` — for rules extending `ModulesRule`
- `ResolvedResourcesValidator` — for rules extending `ResolvedResourcesRule`

#### 1d. Run Tests

```bash
./gradlew :ctp-validators:test
```

#### 1e. Create PR for rmf-codegen

1. Create a branch, stage and commit the 3 files:
   - `<RuleName>Rule.kt`
   - `<rule-name-kebab-case>-rule.raml`
   - `ValidatorRulesTest.groovy`

2. Push and create a PR against `commercetools/rmf-codegen` with:
   - **Title:** `Add <RuleName>Rule validation rule`
   - **Body:** Summary of what the rule validates, list of files changed, test results

Create the PR using `gh` CLI:

```bash
gh pr create --repo commercetools/rmf-codegen \
  --title "Add <RuleName>Rule validation rule" \
  --body "$(cat <<'EOF'
## Summary
- <description of what the rule validates>

## Test plan
- [x] `./gradlew :ctp-validators:test` passes
- [x] Rule catches both prefix and standalone usage
- [x] Exclusions work correctly

🤖 Generated with [Claude Code](https://claude.com/claude-code)
EOF
)"
```

**Fallback** (if `gh` is not installed): use the GitHub REST API with `curl`:

```bash
curl -s -X POST \
  -H "Authorization: token <TOKEN>" \
  -H "Accept: application/vnd.github+json" \
  "https://api.github.com/repos/commercetools/rmf-codegen/pulls" \
  -d '{
    "title": "Add <RuleName>Rule validation rule",
    "body": "## Summary\n...",
    "head": "<fork-owner>:<branch>",
    "base": "main"
  }'
```

---

### Step 2: Detect Violations

Search the API spec RAML files for existing violations.

**Local repos:**
- sphere-backend: `/Users/morass/Repos/sphere-backend` (search-api RAML specs)
- commercetools-docs: `/Users/morass/Repos/commercetools-docs/api-specs/` (main API specs)

**API spec locations in commercetools-docs:**
- `api-specs/api/` — main Composable Commerce API
- `api-specs/history/` — Change History API
- `api-specs/import/` — Import API
- `api-specs/checkout/` — Checkout API
- `api-specs/connect/` — Connect API
- `api-specs/frontend-api/` — Frontend API
- `api-specs/insights/` — Insights API
- `api-specs/mist/` — MIST API

Use grep to search RAML files for violations matching the rule's pattern.

---

### Step 3: Add Exceptions (commercetools-docs)

**Ruleset files:** `api-specs/<api-name>/ruleset.xml`

Add exclusions in the XML format:

```xml
<rule>
    <name>com.commercetools.rmf.validators.<RuleName>Rule</name>
    <options>
        <!--                   TypeName:propertyName -->
        <option type="exclude">TypeName:propertyName</option>
    </options>
</rule>
```

Create a branch, commit, push, and open a PR.

---

### Step 4: Document the Rule (commercetools-docs)

**File:** `api-specs/README.md`

1. Add TOC entry (alphabetically sorted):
   ```
   - [<RuleName>Rule](#<rulename>rule)
   ```

2. Add documentation section in the appropriate subsection of "3. Detailed Rule Descriptions":
   - Section 3.1: RAML-Related Rules
   - Section 3.2: Guidelines Conformity Rules
   ```
   #### <RuleName>Rule

   <Description of what the rule checks>

   ```
     Invalid:
       ...
     Valid:
       ...
   ```
   ```

---

### Step 5: Update Confluence

**Page:** [Spec Validation Rules](https://commercetools.atlassian.net/wiki/spaces/ADGAG/pages/2947514403)
**Cloud ID:** `c6e52965-84b2-4904-af8d-211cbb69dc2c`

Updates needed:
1. Summary table: increment implemented count, decrement not-implemented count
2. Rules table in the current content (use markdown format)
- `updateConfluencePage` to write updated content and save the page adding a version comment with a short summary of the update followed by "Claude Assisted" so it appears in the page history

---

## Reference: Available Case Methods (TypesSwitch)

| Method | Use Case |
|--------|----------|
| `caseObjectType(type: ObjectType)` | Validate object types and their properties |
| `caseProperty(property: Property)` | Validate individual properties |
| `caseStringType(type: StringType)` | Validate string types and enums |
| `caseBooleanType(type: BooleanType)` | Validate boolean types |
| `caseArrayType(type: ArrayType)` | Validate array types |
| `caseNumberType(type: NumberType)` | Validate number types |
| `caseIntegerType(type: IntegerType)` | Validate integer types |
| `caseUnionType(type: UnionType)` | Validate union types |

## Reference: Available Case Methods (ResourcesSwitch)

| Method | Use Case |
|--------|----------|
| `caseMethod(method: Method)` | Validate HTTP methods, query parameters, headers |
| `caseResource(resource: Resource)` | Validate resource URIs and URI parameters |
| `caseBodyContainer(bodyContainer: BodyContainer)` | Validate request/response bodies |

**Accessing query parameters and headers in `caseMethod`:**
- `method.queryParameters` — collection of query parameters
- `method.headers` — collection of headers
- `queryParameter.name` / `header.name` — parameter/header name
- `queryParameter.pattern` — non-null for regex pattern parameters (skip these)

## Reference: Helper Methods

- `create(object, "message {0} {1}", arg0, arg1)` — creates diagnostic with rule's configured severity
- `error(object, "message {0}", arg0)` — creates ERROR diagnostic (ignores configured severity)
- `property.eContainer()` — access parent container (returns ObjectType when called on a property)
- `property.pattern` — non-null for dynamic/pattern properties (e.g., `/a-z/`)
- `StringCaseFormat.LOWER_CAMEL_CASE.apply(name)` — convert to camelCase for comparison

## Reference: Common Exclusion Patterns

| Format | Used By | Example |
|--------|---------|---------|
| `TypeName:propertyName` | BooleanPropertyNameRule, PropertyMinMaxAbbreviationRule | `PriceTier:minimumQuantity` |
| `TypeName:enumValue` | EnumValuePascalCaseRule | `MoneyType:centPrecision` |
| `TypeName` | AsMapRule, EnumValuePascalCaseRule (type-level) | `LocalizedString` |
| `propertyName` | CamelCaseRule, PropertyPluralRule | `error_description` |
| `paramName` | QueryParameterCamelCaseRule, ParameterMinMaxAbbreviationRule | `minimumExcluded` |
| `resourcePath` | ResourcePluralRule | `inventory` |

## Reference: Key Files

| File | Purpose |
|------|---------|
| `ctp-validators/src/main/kotlin/com/commercetools/rmf/validators/` | Rule implementations |
| `ctp-validators/src/test/groovy/com/commercetools/rmf/validators/ValidatorRulesTest.groovy` | All test cases |
| `ctp-validators/src/test/resources/` | Test RAML files |
| `ctp-validators/src/main/resources/ruleset.xml` | Default ruleset (disable rules here) |
| `ctp-validators/src/main/kotlin/com/commercetools/rmf/validators/ValidatorSetup.kt` | Rule discovery mechanism |
| `.github/agents/ctp-validator-rule-creator.agent.md` | Agent instructions (reference) |
