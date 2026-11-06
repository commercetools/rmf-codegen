# Graph Report - rmf-codegen  (2026-11-06)

## Corpus Check
- 421 files · ~124,011 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 204 file(s) not represented in the graph (top: .raml 182, (none) 10, .xml 4)

## Summary
- 4113 nodes · 9693 edges · 283 communities (95 shown, 188 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 292 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `aa637372`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Generator Core Config
- GenerateSubcommand.kt
- Renderer Base Types
- JavaObjectTypeRenderer
- File Producers and Templates
- com.fasterxml.jackson.annotation.JsonCreator
- Bruno Renderers
- ValidateSubcommand.kt
- Method and Body Extensions
- Postman Renderers
- Release Workflow
- LanguageBaseTypes
- Vrap Object File Producers
- Instance Serializers
- TypeScript CTP Client
- Validator Rule Tests
- CodeGenerator.kt
- RAML Markdown Lint
- ResourceRenderer
- OasRenderer
- OAS Path Renderers
- CLI and Project Docs
- Deprecation Diff Checks
- ExtensionsBase
- RuleSeverity
- JavaModelClassFileProducer
- RustTypesFileProducer.kt
- PlantUML Diagrams
- PackageProvider
- Constants and Server Renderers
- Diff Check Tests
- OAS Object Renderers
- io.vrap.codegen.languages.javalang/TestCodeGenerator.kt
- OasProvider
- CLI Diagnostic Printers
- Joi Validator Renderers
- Vrap Extensions
- PHP Object Renderers
- C# Client Builder
- Go File Naming
- GoFileProducer
- Markdown Renderers
- TSLint Config
- Generator Targets and Plugin
- RequestBuilder
- OAS Type Renderers
- Diff Format Printers
- Plural Rules
- Java HTTP Renderers
- Any Type Provider
- Discriminator Rules
- ObjectType
- CI Workflow
- RAML Resource Renderers
- JavaStringHttpRequestRenderer
- RAML Diagnostic Printers
- VrapScalarType
- OAS Serializers
- RAML Doc Serializers
- Generation Targets
- test.php
- VrapTypeProvider
- Diff
- oas/extensions/VrapExtensions.kt
- rules
- ClientConstants
- PythonSchemaRenderer
- RuleSeverity
- JavaModelInterfaceRenderer
- PhpBuilderObjectTypeRenderer
- RustTypeGenerator
- DiffDataType
- ModulesRule
- JavaModelDraftBuilderFileProducer
- PythonModelRenderer
- .allAnyTypes
- TypesRule
- CsharpVrapExtensions.kt
- RMFCommand
- BrunoExtensions.kt
- CsharpHttpRequestRenderer
- .rootResource
- TypeScriptModuleRenderer
- ResourcesRule
- DescriptionFacetExtensions.kt
- RenderingUtils.kt
- OasModuleRenderer.kt
- DiffUtils.kt
- ResourceCatchAllRule
- CsharpRequestBuilderResourceRenderer
- JavaVrapExtensions.kt
- JavaRequestBuilderResourceRenderer
- VrapObjectType
- TypescriptRequestTestRenderer
- .error
- ObjectTypeExtensions
- FilenameRule.kt
- CsharpQueryPredicateRenderer
- PhpFileProducer
- composer.json
- RequestBuilder
- DocsProducer
- BrunoModuleRenderer
- CamelCaseRule
- JavaQueryPredicateRenderer
- PhpMethodRenderer
- NestedTypeRule.kt
- PropertyReference
- OasApiRamlRenderer.kt
- CsharpModelInterfaceRenderer
- ParameterMinMaxAbbreviationRule
- postman/model/Serializers.kt
- TsObjectTypeExtensions
- ResolvedResourcesValidator.kt
- PostBodyRule.kt
- ResourcesValidator.kt
- ModulesValidator.kt
- ValidatorFixtures
- CsharpObjectTypeRenderer
- PhpTraitRenderer
- ApiRamlRenderer.kt
- DiffSubcommand
- RustNames.kt
- DataProvider
- DiffCheck
- Differ
- Scope
- TypesValidator.kt
- CsharpObjectTypeExtensions
- CsharpRequestTestRenderer
- ClientFileProducer
- JoiObjectTypeExtensions.kt
- sortedByTopology
- RootInitFileProducer
- PhpInterfaceUnionTypeRenderer
- PhpUnionTypeRenderer
- AsMapRule
- NamedStringEnumRule
- BaseTest.groovy
- RustTypesTest
- Workflow for Creating a New Rule
- CsharpStringTypeRenderer
- OutputFormat
- DiscriminatedSubtypeRule.kt
- .setup
- ResourceImplExtensions.kt
- CsharpTraitRenderer
- PhpInterfaceObjectTypeRenderer
- .travis.yml
- NamedBodyTypeRule.kt
- DiffType
- .create
- ErrorResponseStructureRule.kt
- OasExtensions.kt
- RuleOption
- QueryParameterCamelCaseRule
- UnionTypePropertyRule
- UpdateActionNameRule
- DiffLanguagesCheckTest
- updates
- TestGoCodeGenerator
- compilerOptions
- Version
- ResourceCollection
- CheckOptionType
- RuleOptionType
- TypeSwitch.kt
- DatetimeRule
- PhpBaseTestFileProducer
- IndexFileProducer.kt
- ClientFileProducer
- ApiRootFileProducer
- ServerHelpers
- extends
- Data Model
- JavaExpansionProducer
- JavaStringTypeExentsions.kt
- install.sh
- install_local.sh
- options
- PlantUmlRelations.kt
- psalm.sh
- ban
- comment-format
- file-header
- no-console
- only-arrow-functions
- strict-boolean-expressions
- triple-equals
- variable-name
- TestCodeGenerator
- rmf-gen.sh
- setup-signing-key.sh
- AnyType
- DomainTypeNameRule
- SuccessBodyRule
- ApiRootFileProducer
- Rust generator
- DiagnosticSeverity
- CLAUDE.md

## God Nodes (most connected - your core abstractions)
1. `TemplateFile` - 317 edges
2. `VrapTypeProvider` - 157 edges
3. `RamlGeneratorModule` - 143 edges
4. `VrapObjectType` - 99 edges
5. `FileProducer` - 85 edges
6. `CodeGeneratorConfig` - 76 edges
7. `VrapType` - 75 edges
8. `Module` - 64 edges
9. `PhpBaseFileProducer` - 60 edges
10. `escapeAll()` - 60 edges

## Surprising Connections (you probably didn't know these)
- `Workflow for Creating a New Rule` --semantically_similar_to--> `Guide to Update CTP Validators`  [INFERRED] [semantically similar]
  .github/agents/ctp-validator-rule-creator.agent.md → ctp-validators/src/main/README.md
- `TestGoCodeGenerator` --calls--> `RamlApiProvider`  [INFERRED]
  languages/go/src/test/kotlin/io/vrap/codegen/languages/TestCodeGenerator.kt → codegen-core/src/main/kotlin/io/vrap/rmf/codegen/di/RamlApiProvider.kt
- `fromCsharpType()` --references--> `VrapObjectType`  [EXTRACTED]
  languages/csharp/src/main/kotlin/io/vrap/codegen/languages/csharp/CsharpBaseTypes.kt → codegen-core/src/main/kotlin/io/vrap/rmf/codegen/types/VrapType.kt
- `toJavaVType()` --calls--> `VrapObjectType`  [INFERRED]
  languages/javalang/java-base/src/main/kotlin/io/vrap/codegen/languages/java/base/extensions/JavaVrapExtensions.kt → codegen-core/src/main/kotlin/io/vrap/rmf/codegen/types/VrapType.kt
- `fromPhpType()` --references--> `VrapObjectType`  [EXTRACTED]
  languages/php/src/main/kotlin/io/vrap/codegen/languages/php/PhpBaseTypes.kt → codegen-core/src/main/kotlin/io/vrap/rmf/codegen/types/VrapType.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **C# Generator Modules** — languages_csharp_readme_csharpmodule, languages_csharp_readme_csharpclientbuildermodule, languages_csharp_readme_csharpmodelinterfacerenderer, languages_csharp_readme_csharpobjecttyperenderer, languages_csharp_readme_csharpstringtyperenderer, languages_csharp_readme_csharptraitrenderer, languages_csharp_readme_csharphttprequestrenderer, languages_csharp_readme_csharprequestbuilderrenderer [EXTRACTED 1.00]
- **Java Generator Modules** — languages_javalang_builder_renderer_java_builder_client_readme_javacompletemodule, languages_javalang_builder_renderer_java_builder_client_readme_javatestmodule, languages_javalang_builder_renderer_java_builder_client_readme_javamodelinterfacerenderer, languages_javalang_builder_renderer_java_builder_client_readme_javahttprequestrenderer, languages_javalang_builder_renderer_java_builder_client_readme_javarequestbuilderresourcerenderer, languages_javalang_builder_renderer_java_builder_client_readme_javarequesttestrenderer [EXTRACTED 1.00]
- **Validator Rule Creation Workflow** — github_agents_ctp_validator_rule_creator_agent_workflow_for_creating_a_new_rule, github_agents_ctp_validator_rule_creator_agent_booleanpropertynamerule, ctp_validators_src_test_groovy_com_commercetools_rmf_validators_validatorrulestest, github_agents_ctp_validator_rule_creator_agent_test_raml_files, github_agents_ctp_validator_rule_creator_agent_typesrule [EXTRACTED 1.00]

## Communities (283 total, 188 thin omitted)

### Community 0 - "Generator Core Config"
Cohesion: 0.07
Nodes (15): CodeGeneratorConfig, RamlApiProvider, RamlGeneratorComponent, RamlGeneratorModule, MemoryDataSink, TestCodeGenerator, TestCodeGenerator, BuilderTestCodeGenerator (+7 more)

### Community 1 - "GenerateSubcommand.kt"
Cohesion: 0.04
Nodes (28): Module, CodeGenerator, FileGenerator, BrunoModelModule, CsharpClientBuilderModule, CsharpModule, CsharpQueryPredicateModule, CsharpTestModule (+20 more)

### Community 2 - "Renderer Base Types"
Cohesion: 0.11
Nodes (16): MethodRenderer, ObjectTypeRenderer, escapeAll(), keepAngleIndent(), keepCurlyIndent(), keepIndentation(), firstLowerCase(), firstUpperCase() (+8 more)

### Community 5 - "com.fasterxml.jackson.annotation.JsonCreator"
Cohesion: 0.06
Nodes (7): Apply, Check, CheckOption, DiffConfiguration, Apply, RuleSet, DiffSetup

### Community 6 - "Bruno Renderers"
Cohesion: 0.06
Nodes (6): BrunoActionRenderer, BrunoMethodRenderer, BrunoRequestRenderer, BrunoUrl, InstanceSerializer, ObjectInstanceSerializer

### Community 7 - "ValidateSubcommand.kt"
Cohesion: 0.06
Nodes (3): executeAndMeasureTimeMillis(), toSeconds(), BuildLogicFunctionalTest

### Community 8 - "Method and Body Extensions"
Cohesion: 0.09
Nodes (15): hasPathParams(), hasReturnPayload(), isSuccessfull(), resource(), returnType(), toRequestName(), csharpReturnType(), bodyType() (+7 more)

### Community 9 - "Postman Renderers"
Cohesion: 0.07
Nodes (9): ActionRenderer, auth(), authorization(), jScript(), testAuthScript(), MethodRenderer, PostmanModuleRenderer, PostmanUrl (+1 more)

### Community 10 - "Release Workflow"
Cohesion: 0.05
Nodes (50): if, name, needs, permissions, runs-on, steps, GITHUB_TOKEN, jobs (+42 more)

### Community 11 - "LanguageBaseTypes"
Cohesion: 0.04
Nodes (22): GeneratorModule, OasGeneratorModule, UserProvidedResourcesFilterSwitch, DataSink, FileDataSink, LanguageBaseTypes, BrunoBaseTypes, nativePostmanType() (+14 more)

### Community 12 - "Vrap Object File Producers"
Cohesion: 0.12
Nodes (9): allMethods(), deprecated(), toJoiPackageName(), toJoiVrapType(), toRequestBuilderName(), toTsComment(), toTsCommentList(), tsRequestModuleName() (+1 more)

### Community 13 - "Instance Serializers"
Cohesion: 0.12
Nodes (24): getParent(), InstanceSerializer, isScalar(), normalize(), NullInstanceSerializer, ObjectInstanceSerializer, renderAnnotation(), renderArrayType() (+16 more)

### Community 14 - "TypeScript CTP Client"
Cohesion: 0.06
Nodes (42): dependencies, @commercetools/sdk-client@^2.1.1, @commercetools/sdk-middleware-auth@^6.0.0, @commercetools/sdk-middleware-http@^6.0.0, joi@^14.3.1, node-fetch@^2.6.0, devDependencies, prettier@^1.18.2 (+34 more)

### Community 15 - "Validator Rule Tests"
Cohesion: 0.04
Nodes (47): ValidatorRulesTest, "asMap annotation rule", "boolean property name rule", "datetime rule", "discriminator name rule", "discriminator parent rule", "discriminator subtype rule", "discriminator value rule" (+39 more)

### Community 16 - "CodeGenerator.kt"
Cohesion: 0.05
Nodes (25): CodeGeneratorImpl, ComposedSchemaGenerator, MethodGenerator, NamedScalarTypeGenerator, NamedStringTypeGenerator, ObjectSchemaGenerator, ObjectTypeGenerator, PathItemGenerator (+17 more)

### Community 17 - "RAML Markdown Lint"
Cohesion: 0.05
Nodes (38): IncludeYamlType, jsYamlSchema, markdownLintDefaults, prettifyRamlPath(), run(), validateMarkdown(), yamlLoad(), bin (+30 more)

### Community 18 - "ResourceRenderer"
Cohesion: 0.13
Nodes (4): ResourceRenderer, AbstractRequestBuilder, PhpMethodBuilderRenderer, RequestBuilderInit

### Community 21 - "CLI and Project Docs"
Cohesion: 0.05
Nodes (41): @commercetools-docs/rmf-codegen, Query Parameter Double Quote Fix, ramldoc-generator, 12.0.0 Release, rmf-codegen 0.18 Upgrade, rmf-codegen 0.1.10 Update, rmf-codegen 0.1.5 Update, rmf-codegen 0.1.7 Update (+33 more)

### Community 22 - "Deprecation Diff Checks"
Cohesion: 0.16
Nodes (27): CheckSeverity, ERROR, FATAL, INFO, WARN, DeprecatedAddedCheck, DeprecatedAddedMethodCheck, DeprecatedAddedResourceCheck (+19 more)

### Community 23 - "ExtensionsBase"
Cohesion: 0.11
Nodes (6): ExtensionsBase, packageDir(), OasScalarTypeRenderer, RamlObjectTypeRenderer, EObjectTypeExtensions, RamlObjectTypeRenderer

### Community 24 - "RuleSeverity"
Cohesion: 0.08
Nodes (7): Rule, RuleOption, RuleSeverity, ERROR, INFO, WARN, ValidatorFactory

### Community 26 - "RustTypesFileProducer.kt"
Cohesion: 0.10
Nodes (6): RustKind, ALIAS, MAP, STRING_ENUM, STRUCT, UNION

### Community 28 - "PackageProvider"
Cohesion: 0.16
Nodes (3): PackageProvider, ResourcePackageSwitch, TypePackageSwitch

### Community 29 - "Constants and Server Renderers"
Cohesion: 0.10
Nodes (4): VrapLibraryType, ConstantsProvider, ParameterGenerator, ServerRenderer

### Community 30 - "Diff Check Tests"
Cohesion: 0.06
Nodes (34): DiffCheckTest, "added enum", "added method", "added property", "added query parameter", "added resource", "added type", "deprecated method" (+26 more)

### Community 31 - "OAS Object Renderers"
Cohesion: 0.10
Nodes (16): format(), numberType(), OasObjectTypeRenderer, renderAny(), renderAnyType(), renderArray(), renderBoolean(), renderDateOnly() (+8 more)

### Community 32 - "io.vrap.codegen.languages.javalang/TestCodeGenerator.kt"
Cohesion: 0.11
Nodes (10): fromDefaultJavaType(), fromJavaType(), JavaBaseTypes, PlantUmlBaseTypes, primitiveType(), scalarType(), GroovyDslModule, JavaModelModule (+2 more)

### Community 33 - "OasProvider"
Cohesion: 0.12
Nodes (5): ApiProvider, GeneratorComponent, OasGeneratorComponent, OasProvider, CoreCodeGenerator

### Community 34 - "CLI Diagnostic Printers"
Cohesion: 0.14
Nodes (11): LinkFormat, CLI, GITHUB, CliFormatPrinter, CliLinkFormatter, FormatPrinter, GithubFormatPrinter, GithubLinkFormatter (+3 more)

### Community 35 - "Joi Validator Renderers"
Cohesion: 0.16
Nodes (3): toVrapType(), JoiValidatorModuleRenderer, PropertiesComparator

### Community 36 - "Vrap Extensions"
Cohesion: 0.10
Nodes (23): accessTokenUri(), accessTokenUriParams(), allParams(), apiResource(), firstBody(), fullClassName(), isScalar(), methodName() (+15 more)

### Community 38 - "C# Client Builder"
Cohesion: 0.08
Nodes (32): CsharpClientBuilderModule, CsharpHttpRequestRenderer, CsharpModelInterfaceRenderer, CsharpModule, CsharpObjectTypeRenderer, CsharpRequestBuilderRenderer, CsharpStringTypeRenderer, CsharpTraitRenderer (+24 more)

### Community 39 - "Go File Naming"
Cohesion: 0.06
Nodes (9): exportName(), goModelFileName(), goName(), snakeCase(), ApiRootFileProducer, firstBody(), resourcePathList(), simpleTSName() (+1 more)

### Community 40 - "GoFileProducer"
Cohesion: 0.05
Nodes (7): ClientConstants, toStructName(), GoMethodRenderer, RequestBuilder, toBlockComment(), GoObjectTypeExtensions, GoFileProducer

### Community 42 - "TSLint Config"
Cohesion: 0.07
Nodes (29): rules, completed-docs, cyclomatic-complexity, increment-decrement, interface-name, linebreak-style, max-classes-per-file, member-access (+21 more)

### Community 43 - "Generator Targets and Plugin"
Cohesion: 0.11
Nodes (16): RamlGenerator, Target, TargetType, GO_CLIENT_COMPLETE, GROOVY_DSL, JAVA_API_BUILDER, JAVA_COMPLETE, JAVA_MODEL (+8 more)

### Community 45 - "OAS Type Renderers"
Cohesion: 0.15
Nodes (3): OasComposedTypeRenderer, OasObjectTypeRenderer, OasTypeRenderer

### Community 46 - "Diff Format Printers"
Cohesion: 0.15
Nodes (14): CliFormatPrinter, DotNetMarkdownFormatPrinter, firstUpperCase(), FormatPrinter, getMethodName(), JavaMarkdownFormatPrinter, JsonFormatPrinter, LanguageMarkdownFormatPrinter (+6 more)

### Community 47 - "Plural Rules"
Cohesion: 0.11
Nodes (5): PropertyPluralRule, ResourcePluralRule, ValidatorSet, ValidatorSets, StringPropertySingularRule

### Community 50 - "Discriminator Rules"
Cohesion: 0.09
Nodes (4): DiscriminatorParentRule, DiscriminatorValueRule, PackageDefinedRule, PolymorphicSubtypesRule

### Community 52 - "CI Workflow"
Cohesion: 0.10
Nodes (23): jobs, test, test_node, name, on, permissions, continue-on-error, name (+15 more)

### Community 54 - "JavaStringHttpRequestRenderer"
Cohesion: 0.05
Nodes (7): JavaExpansionPredicateRenderer, JavaStringHttpRequestRenderer, JavaBuilderTestRenderer, JavaRequestTestRenderer, getImportsForType(), JavaObjectTypeExtensions, JavaStringTypeRenderer

### Community 55 - "RAML Diagnostic Printers"
Cohesion: 0.15
Nodes (7): ConsoleDataSink, DefaultRamlDiagnosticPrinter, GithubRamlDiagnosticPrinter, Printer, GITHUB, LOGGER, RamlDiagnosticPrinter

### Community 56 - "VrapScalarType"
Cohesion: 0.07
Nodes (15): DateTimeTypes, DateOnly, DateTime, TimeOnly, VrapAnyType, VrapDateTimeType, VrapScalarType, fromCsharpType() (+7 more)

### Community 57 - "OAS Serializers"
Cohesion: 0.17
Nodes (6): ExampleSerializer, InlineTypeSerializer, OasModuleRenderer, ReferenceSerializer, RegExpSerializer, UriTemplateSerializer

### Community 58 - "RAML Doc Serializers"
Cohesion: 0.17
Nodes (6): ExampleSerializer, InlineTypeSerializer, RamldocModuleRenderer, ReferenceSerializer, RegExpSerializer, UriTemplateSerializer

### Community 59 - "Generation Targets"
Cohesion: 0.08
Nodes (24): GenerationTarget, BRUNO, CSHARP_CLIENT, CSHARP_QUERY_PREDICATES, CSHARP_TEST, DOC_MARKDOWN, GO_CLIENT, JAVA_CLIENT (+16 more)

### Community 61 - "VrapTypeProvider"
Cohesion: 0.06
Nodes (17): FileProducer, VrapEnumType, VrapTypeProvider, getSuperTypes(), EObjectExtensions, getMethodName(), toResourceName(), ExtensionsBase (+9 more)

### Community 62 - "Diff"
Cohesion: 0.18
Nodes (7): EnumAddedCheck, EnumRemovedCheck, Diff, DiffData, Position, Source, getSource()

### Community 63 - "oas/extensions/VrapExtensions.kt"
Cohesion: 0.12
Nodes (19): getParent(), InstanceSerializer, ObjectInstanceSerializer, renderAnnotation(), renderArrayType(), renderBaseUriParameter(), renderEAttributes(), renderExample() (+11 more)

### Community 64 - "rules"
Cohesion: 0.09
Nodes (21): extends, options, rules, await-promise, deprecation, match-default-export-name, no-boolean-literal-compare, no-floating-promises (+13 more)

### Community 65 - "ClientConstants"
Cohesion: 0.17
Nodes (4): UnionTypeRenderer, ClientConstants, PhpCollectionRenderer, PhpUnionCollectionRenderer

### Community 67 - "RuleSeverity"
Cohesion: 0.13
Nodes (4): MethodResponseRule, ResolvedResourcesRule, UriParameterDeclaredRule, Validator

### Community 70 - "RustTypeGenerator"
Cohesion: 0.26
Nodes (4): Entry, RustTypeGenerator, pascalCase(), requireIdent()

### Community 71 - "DiffDataType"
Cohesion: 0.12
Nodes (17): PropertyAddedCheck, PropertyRemovedCheck, DiffDataType, ANY_TYPES, ANY_TYPES_MAP, API, METHOD_RESPONSE_TYPES_MAP, METHOD_TYPES_MAP (+9 more)

### Community 72 - "ModulesRule"
Cohesion: 0.15
Nodes (4): DiagnosticsAware, DiagnosticsCreator, ModulesRule, SdkBaseUriRule

### Community 76 - "TypesRule"
Cohesion: 0.15
Nodes (3): BooleanPropertyNameRule, EnumValuePascalCaseRule, TypesRule

### Community 77 - "CsharpVrapExtensions.kt"
Cohesion: 0.17
Nodes (10): csharpClassRelativePath(), csharpPackage(), firstBody(), pluralize(), requestBuildersPackage(), resourcePathList(), simpleName(), singularize() (+2 more)

### Community 78 - "RMFCommand"
Cohesion: 0.15
Nodes (7): main(), RMFCommand, LogLevel, DEBUG, ERROR, INFO, WARN

### Community 79 - "BrunoExtensions.kt"
Cohesion: 0.15
Nodes (8): description(), escapeJson(), getParent(), oAuth2(), description(), escapeJson(), getParent(), oAuth2()

### Community 81 - ".rootResource"
Cohesion: 0.19
Nodes (3): PhpRequestTestRenderer, PhpTestModule, PhpTestRenderer

### Community 83 - "ResourcesRule"
Cohesion: 0.15
Nodes (3): HeadBodyRule, QueryParameterPlaceholderAnnotationRule, ResourcesRule

### Community 84 - "DescriptionFacetExtensions.kt"
Cohesion: 0.14
Nodes (4): toHtml(), filterLinks(), toHtml(), toLineComment()

### Community 85 - "RenderingUtils.kt"
Cohesion: 0.20
Nodes (11): generateTemplate(), IndentationTokens, IndentMarker, DoubleAngle, DoubleCurly, SingleAngle, SingleCurly, indentString() (+3 more)

### Community 88 - "DiffUtils.kt"
Cohesion: 0.05
Nodes (19): NamedScalarTypeRenderer, NamedStringTypeRenderer, PatternStringTypeRenderer, allAnyTypes(), allResourceMethods(), toAnyTypeMap(), toInstanceMap(), toMethodMap() (+11 more)

### Community 91 - "JavaVrapExtensions.kt"
Cohesion: 0.13
Nodes (9): JavaEObjectTypeExtensions, firstBody(), fullClassName(), isExpandable(), resourcePathList(), simpleName(), toJavaPackage(), toJavaVType() (+1 more)

### Community 93 - "VrapObjectType"
Cohesion: 0.06
Nodes (20): ResourcesTypeProvider, VrapArrayType, VrapNilType, VrapObjectType, VrapType, CsharpEObjectTypeExtensions, toCsharpVType(), flattenVrapType() (+12 more)

### Community 95 - ".error"
Cohesion: 0.22
Nodes (3): InternalLogger, safeRun(), VerifySubcommand

### Community 96 - "ObjectTypeExtensions"
Cohesion: 0.44
Nodes (3): getImportsForType(), namedSubTypes(), ObjectTypeExtensions

### Community 100 - "composer.json"
Cohesion: 0.14
Nodes (13): autoload, psr-4, description, license, name, Commercetools\\, Commercetools\\Test\\, repositories (+5 more)

### Community 108 - "PropertyReference"
Cohesion: 0.36
Nodes (6): DeprecatedAddedPropertyCheck, MarkDeprecatedRemovedPropertyCheck, PropertyOptionalCheck, PropertyRequiredCheck, PropertyTypeChangedCheck, PropertyReference

### Community 112 - "postman/model/Serializers.kt"
Cohesion: 0.35
Nodes (3): toJson(), InstanceSerializer, ObjectInstanceSerializer

### Community 118 - "ValidatorFixtures"
Cohesion: 0.16
Nodes (4): ValidatorFixtures, ValidatorSetupTest, ValidatorSetupTest, ValidatorFixtures

### Community 123 - "RustNames.kt"
Cohesion: 0.47
Nodes (4): camelCase(), moduleNameOf(), rustIdent(), snakeCase()

### Community 124 - "DataProvider"
Cohesion: 0.27
Nodes (6): DataProvider, resource(), MethodBodyTypeChangedCheck, MethodResponseBodyTypeChangedCheck, MethodBodyTypeReference, MethodResponseBodyTypeReference

### Community 125 - "DiffCheck"
Cohesion: 0.36
Nodes (5): DiffCheck, MethodAddedCheck, MethodRemovedCheck, QueryParameterAddedCheck, QueryParameterRemovedCheck

### Community 126 - "Differ"
Cohesion: 0.33
Nodes (3): Differ, Builder, RamlDiff

### Community 127 - "Scope"
Cohesion: 0.20
Nodes (10): Scope, ENUM, METHOD, METHOD_BODY, METHOD_RESPONSE_BODY, PROPERTY, QUERY_PARAMETER, RESOURCE (+2 more)

### Community 140 - "BaseTest.groovy"
Cohesion: 0.33
Nodes (3): BaseTest, DiffSetupTest, "default set"

### Community 142 - "Workflow for Creating a New Rule"
Cohesion: 0.16
Nodes (14): ctp-validators Ruleset, Guide to Update CTP Validators, ResourcePluralRule, Rule Description Page, Validator Rules Folder, Validator Test Examples, BooleanPropertyNameRule, commercetools-docs Validator Rules Page (+6 more)

### Community 144 - "OutputFormat"
Cohesion: 0.22
Nodes (9): OutputFormat, CLI, DOTNET_MARKDOWN, GITHUB, JAVA_MARKDOWN, JSON, MARKDOWN, PHP_MARKDOWN (+1 more)

### Community 151 - ".travis.yml"
Cohesion: 0.25
Nodes (7): before_cache, cache, directories, jdk, language, script, services

### Community 153 - "DiffType"
Cohesion: 0.29
Nodes (7): DiffType, ADDED, CHANGED, DEPRECATED, MARK_DEPRECATED, REMOVED, REQUIRED

### Community 159 - "OasExtensions.kt"
Cohesion: 0.22
Nodes (3): toParamName(), renderUriParameter(), toYaml()

### Community 167 - "DiffLanguagesCheckTest"
Cohesion: 0.29
Nodes (6): DiffLanguagesCheckTest, "added method", "added method get category dotnet", "added method get category java", "added method get category php", "added method get category ts"

### Community 168 - "updates"
Cohesion: 0.29
Nodes (6): interval, updates, directory, package-ecosystem, schedule, version

### Community 171 - "compilerOptions"
Cohesion: 0.29
Nodes (6): compilerOptions, module, outDir, target, exclude, include

### Community 192 - "extends"
Cohesion: 0.40
Nodes (4): extends, tslint-config-prettier, tslint-plugin-prettier, tslint:all

### Community 200 - "install.sh"
Cohesion: 0.83
Nodes (3): installVrapCli(), install.sh script, uninstallVrapCli()

### Community 201 - "install_local.sh"
Cohesion: 0.83
Nodes (3): installRmfCli(), install_local.sh script, uninstallRmfCli()

### Community 203 - "options"
Cohesion: 0.67
Nodes (3): options, order, member-ordering

### Community 287 - "AnyType"
Cohesion: 0.43
Nodes (3): TypeAddedCheck, TypeChangedCheck, TypeRemovedCheck

### Community 290 - "ApiRootFileProducer"
Cohesion: 0.21
Nodes (4): ApiRootFileProducer, ClientConstants, lowerCasePackage(), TypescriptClientModule

### Community 294 - "Rust generator"
Cohesion: 0.50
Nodes (3): Not yet, Rust generator, What is generated

### Community 295 - "DiagnosticSeverity"
Cohesion: 0.50
Nodes (4): DiagnosticSeverity, ERROR, INFO, WARN

## Ambiguous Edges - Review These
- `JAVA_CLIENT` → `Gradle Target Values`  [AMBIGUOUS]
  tools/codegen-gradle-plugin/README.md · relation: conceptually_related_to
- `TYPESCRIPT_CLIENT` → `Gradle Target Values`  [AMBIGUOUS]
  tools/codegen-gradle-plugin/README.md · relation: conceptually_related_to
- `rmf-codegen 0.18 Upgrade` → `rmf-codegen 0.1.10 Update`  [AMBIGUOUS]
  node/rmf-codegen/CHANGELOG.md · relation: conceptually_related_to

## Knowledge Gaps
- **231 isolated node(s):** `STRUCT`, `MAP`, `UNION`, `STRING_ENUM`, `ALIAS` (+226 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 918 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **188 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `TemplateFile` connect `File Producers and Templates` to `Renderer Base Types`, `JavaObjectTypeRenderer`, `Bruno Renderers`, `ValidateSubcommand.kt`, `Method and Body Extensions`, `Postman Renderers`, `LanguageBaseTypes`, `Vrap Object File Producers`, `CodeGenerator.kt`, `ResourceRenderer`, `OasRenderer`, `OAS Path Renderers`, `ExtensionsBase`, `JavaModelClassFileProducer`, `RustTypesFileProducer.kt`, `PlantUML Diagrams`, `Constants and Server Renderers`, `OAS Object Renderers`, `OasProvider`, `Joi Validator Renderers`, `PHP Object Renderers`, `Go File Naming`, `GoFileProducer`, `Markdown Renderers`, `RequestBuilder`, `OAS Type Renderers`, `Java HTTP Renderers`, `RAML Resource Renderers`, `JavaStringHttpRequestRenderer`, `RAML Diagnostic Printers`, `VrapScalarType`, `OAS Serializers`, `RAML Doc Serializers`, `VrapTypeProvider`, `ClientConstants`, `PythonSchemaRenderer`, `JavaModelInterfaceRenderer`, `PhpBuilderObjectTypeRenderer`, `RustTypeGenerator`, `JavaModelDraftBuilderFileProducer`, `PythonModelRenderer`, `CsharpHttpRequestRenderer`, `.rootResource`, `TypeScriptModuleRenderer`, `OasModuleRenderer.kt`, `DiffUtils.kt`, `CsharpRequestBuilderResourceRenderer`, `JavaVrapExtensions.kt`, `JavaRequestBuilderResourceRenderer`, `VrapObjectType`, `TypescriptRequestTestRenderer`, `CsharpQueryPredicateRenderer`, `PhpFileProducer`, `RequestBuilder`, `DocsProducer`, `BrunoModuleRenderer`, `JavaQueryPredicateRenderer`, `PhpMethodRenderer`, `OasApiRamlRenderer.kt`, `CsharpModelInterfaceRenderer`, `CsharpObjectTypeRenderer`, `PhpTraitRenderer`, `ApiRamlRenderer.kt`, `CsharpRequestTestRenderer`, `ClientFileProducer`, `RootInitFileProducer`, `PhpInterfaceUnionTypeRenderer`, `PhpUnionTypeRenderer`, `CsharpStringTypeRenderer`, `CsharpTraitRenderer`, `PhpInterfaceObjectTypeRenderer`, `PhpBaseTestFileProducer`, `IndexFileProducer.kt`, `ClientFileProducer`, `ApiRootFileProducer`, `ServerHelpers`, `JavaExpansionProducer`, `TemplateFile.kt`, `ApiRootFileProducer`?**
  _High betweenness centrality (0.155) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `VrapTypeProvider` (e.g. with `AnyTypeProvider` and `ResourcesTypeProvider`) actually correct?**
  _`VrapTypeProvider` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `STRUCT`, `MAP`, `UNION` to the rest of the system?**
  _231 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Generator Core Config` be split into smaller, more focused modules?**
  _Cohesion score 0.06720263863121007 - nodes in this community are weakly interconnected._
- **Why does `VrapTypeProvider` connect `VrapTypeProvider` to `CsharpObjectTypeExtensions`, `Renderer Base Types`, `CsharpRequestTestRenderer`, `JavaObjectTypeRenderer`, `Bruno Renderers`, `PhpInterfaceUnionTypeRenderer`, `.providePackageName`, `Method and Body Extensions`, `PhpUnionTypeRenderer`, `LanguageBaseTypes`, `Postman Renderers`, `RootInitFileProducer`, `Vrap Object File Producers`, `CsharpStringTypeRenderer`, `CodeGenerator.kt`, `ResourceRenderer`, `OasRenderer`, `CsharpTraitRenderer`, `PhpInterfaceObjectTypeRenderer`, `ExtensionsBase`, `JavaModelClassFileProducer`, `RustTypesFileProducer.kt`, `PlantUML Diagrams`, `Constants and Server Renderers`, `OAS Object Renderers`, `ApiRootFileProducer`, `Joi Validator Renderers`, `PHP Object Renderers`, `Go File Naming`, `GoFileProducer`, `Markdown Renderers`, `RequestBuilder`, `Java HTTP Renderers`, `Any Type Provider`, `ObjectType`, `RAML Resource Renderers`, `JavaStringHttpRequestRenderer`, `IndexFileProducer.kt`, `OAS Serializers`, `RAML Doc Serializers`, `ClientConstants`, `PythonSchemaRenderer`, `JavaModelInterfaceRenderer`, `PhpBuilderObjectTypeRenderer`, `RustTypeGenerator`, `JavaModelDraftBuilderFileProducer`, `PythonModelRenderer`, `CsharpHttpRequestRenderer`, `TypeScriptModuleRenderer`, `OasModuleRenderer.kt`, `DiffUtils.kt`, `CsharpRequestBuilderResourceRenderer`, `JavaVrapExtensions.kt`, `JavaRequestBuilderResourceRenderer`, `VrapObjectType`, `TypescriptRequestTestRenderer`, `CsharpQueryPredicateRenderer`, `RequestBuilder`, `BrunoModuleRenderer`, `JavaQueryPredicateRenderer`, `PhpMethodRenderer`, `CsharpModelInterfaceRenderer`, `CsharpObjectTypeRenderer`, `PhpTraitRenderer`, `ApiRamlRenderer.kt`?**
  _High betweenness centrality (0.138) - this node is a cross-community bridge._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `JAVA_CLIENT` and `Gradle Target Values`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `TYPESCRIPT_CLIENT` and `Gradle Target Values`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._