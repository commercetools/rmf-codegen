# Graph Report - rmf-codegen  (2026-11-06)

## Corpus Check
- 412 files · ~119,819 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 203 file(s) not represented in the graph (top: .raml 181, (none) 10, .xml 4)

## Summary
- 4006 nodes · 9411 edges · 287 communities (94 shown, 193 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 273 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Generator Core Config
- DI Module Wiring
- Renderer Base Types
- Trait and Object Renderers
- File Producers and Templates
- Diff and Apply Checks
- Bruno Renderers
- Generator Components
- Method and Body Extensions
- Postman Renderers
- Release Workflow
- Data Sinks and API Providers
- Vrap Object File Producers
- Instance Serializers
- TypeScript CTP Client
- Validator Rule Tests
- Code Generator Impl
- RAML Markdown Lint
- PHP Request Builders
- OAS Renderers
- OAS Path Renderers
- CLI and Project Docs
- Deprecation Diff Checks
- Type Providers and Groovy DSL
- Validator Rules
- Java Renderers
- Go Client Renderers
- PlantUML Diagrams
- Vrap Type Model
- Constants and Server Renderers
- Diff Check Tests
- OAS Object Renderers
- Language Base Types
- RAML Example Renderers
- CLI Diagnostic Printers
- Joi Validator Renderers
- Vrap Extensions
- PHP Object Renderers
- C# Client Builder
- Go File Naming
- Go File Producers
- Markdown Renderers
- TSLint Config
- Generator Targets and Plugin
- Request Builder Renderers
- OAS Type Renderers
- Diff Format Printers
- Plural Rules
- Java HTTP Renderers
- Any Type Provider
- Discriminator Rules
- Scalar Type Mapping
- CI Workflow
- RAML Resource Renderers
- Java String HTTP Renderers
- RAML Diagnostic Printers
- Date Time and PHP Types
- OAS Serializers
- RAML Doc Serializers
- Generation Targets
- Community 60
- Community 61
- Community 62
- Community 63
- Community 64
- Community 65
- Community 66
- Community 67
- Community 68
- Community 69
- Community 70
- Community 71
- Community 72
- Community 73
- Community 74
- Community 76
- Community 77
- Community 78
- Community 79
- Community 80
- Community 81
- Community 82
- Community 83
- Community 84
- Community 85
- Community 86
- Community 88
- Community 89
- Community 90
- Community 91
- Community 92
- Community 93
- Community 94
- Community 95
- Community 96
- Community 97
- Community 98
- Community 99
- Community 100
- Community 101
- Community 102
- Community 103
- Community 104
- Community 105
- Community 106
- Community 107
- Community 108
- Community 109
- Community 110
- Community 111
- Community 112
- Community 113
- Community 114
- Community 115
- Community 116
- Community 117
- Community 118
- Community 119
- Community 120
- Community 121
- Community 122
- Community 123
- Community 124
- Community 125
- Community 126
- Community 127
- Community 128
- Community 129
- Community 130
- Community 131
- Community 132
- Community 133
- Community 134
- Community 135
- Community 138
- Community 139
- Community 140
- Community 141
- Community 142
- Community 143
- Community 144
- Community 145
- Community 146
- Community 147
- Community 148
- Community 149
- Community 150
- Community 151
- Community 152
- Community 153
- Community 154
- Community 155
- Community 156
- Community 157
- Community 158
- Community 159
- Community 160
- Community 161
- Community 162
- Community 163
- Community 164
- Community 165
- Community 166
- Community 167
- Community 168
- Community 169
- Community 170
- Community 171
- Community 173
- Community 174
- Community 176
- Community 177
- Community 178
- Community 179
- Community 180
- Community 181
- Community 182
- Community 183
- Community 184
- Community 185
- Community 189
- Community 190
- Community 191
- Community 192
- Community 193
- Community 198
- Community 199
- Community 200
- Community 201
- Community 202
- Community 203
- Community 205
- Community 206
- Community 207
- Community 208
- Community 209
- Community 210
- Community 211
- Community 212
- Community 213
- Community 214
- Community 215
- Community 216
- Community 217

## God Nodes (most connected - your core abstractions)
1. `TemplateFile` - 313 edges
2. `VrapTypeProvider` - 154 edges
3. `RamlGeneratorModule` - 139 edges
4. `VrapObjectType` - 100 edges
5. `FileProducer` - 83 edges
6. `CodeGeneratorConfig` - 74 edges
7. `VrapType` - 73 edges
8. `Module` - 62 edges
9. `escapeAll()` - 60 edges
10. `PhpBaseFileProducer` - 60 edges

## Surprising Connections (you probably didn't know these)
- `Workflow for Creating a New Rule` --semantically_similar_to--> `Guide to Update CTP Validators`  [INFERRED] [semantically similar]
  .github/agents/ctp-validator-rule-creator.agent.md → ctp-validators/src/main/README.md
- `TestGoCodeGenerator` --calls--> `RamlApiProvider`  [INFERRED]
  languages/go/src/test/kotlin/io/vrap/codegen/languages/TestCodeGenerator.kt → codegen-core/src/main/kotlin/io/vrap/rmf/codegen/di/RamlApiProvider.kt
- `fromCsharpType()` --references--> `VrapObjectType`  [EXTRACTED]
  languages/csharp/src/main/kotlin/io/vrap/codegen/languages/csharp/CsharpBaseTypes.kt → codegen-core/src/main/kotlin/io/vrap/rmf/codegen/types/VrapType.kt
- `toCsharpVType()` --calls--> `VrapObjectType`  [INFERRED]
  languages/csharp/src/main/kotlin/io/vrap/codegen/languages/csharp/extensions/CsharpVrapExtensions.kt → codegen-core/src/main/kotlin/io/vrap/rmf/codegen/types/VrapType.kt
- `toJavaVType()` --calls--> `VrapObjectType`  [INFERRED]
  languages/javalang/java-base/src/main/kotlin/io/vrap/codegen/languages/java/base/extensions/JavaVrapExtensions.kt → codegen-core/src/main/kotlin/io/vrap/rmf/codegen/types/VrapType.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Validator Rule Creation Workflow** — github_agents_ctp_validator_rule_creator_agent_workflow_for_creating_a_new_rule, github_agents_ctp_validator_rule_creator_agent_booleanpropertynamerule, ctp_validators_src_test_groovy_com_commercetools_rmf_validators_validatorrulestest, github_agents_ctp_validator_rule_creator_agent_test_raml_files, github_agents_ctp_validator_rule_creator_agent_typesrule [EXTRACTED 1.00]
- **C# Generator Modules** — languages_csharp_readme_csharpmodule, languages_csharp_readme_csharpclientbuildermodule, languages_csharp_readme_csharpmodelinterfacerenderer, languages_csharp_readme_csharpobjecttyperenderer, languages_csharp_readme_csharpstringtyperenderer, languages_csharp_readme_csharptraitrenderer, languages_csharp_readme_csharphttprequestrenderer, languages_csharp_readme_csharprequestbuilderrenderer [EXTRACTED 1.00]
- **Java Generator Modules** — languages_javalang_builder_renderer_java_builder_client_readme_javacompletemodule, languages_javalang_builder_renderer_java_builder_client_readme_javatestmodule, languages_javalang_builder_renderer_java_builder_client_readme_javamodelinterfacerenderer, languages_javalang_builder_renderer_java_builder_client_readme_javahttprequestrenderer, languages_javalang_builder_renderer_java_builder_client_readme_javarequestbuilderresourcerenderer, languages_javalang_builder_renderer_java_builder_client_readme_javarequesttestrenderer [EXTRACTED 1.00]

## Communities (287 total, 193 thin omitted)

### Community 0 - "Generator Core Config"
Cohesion: 0.06
Nodes (14): CodeGeneratorConfig, RamlApiProvider, RamlGeneratorComponent, MemoryDataSink, BrunoBaseTypes, TestCodeGenerator, TestCodeGenerator, BuilderTestCodeGenerator (+6 more)

### Community 1 - "DI Module Wiring"
Cohesion: 0.06
Nodes (28): Module, RamlGeneratorModule, FileGenerator, ResourceGenerator, BrunoModelModule, CsharpClientBuilderModule, CsharpModule, CsharpQueryPredicateModule (+20 more)

### Community 2 - "Renderer Base Types"
Cohesion: 0.10
Nodes (15): MethodRenderer, ObjectTypeRenderer, escapeAll(), keepAngleIndent(), keepCurlyIndent(), keepIndentation(), firstLowerCase(), firstUpperCase() (+7 more)

### Community 3 - "Trait and Object Renderers"
Cohesion: 0.05
Nodes (9): TraitRenderer, CsharpTraitRenderer, CascadeValidationCheck, JavaObjectTypeRenderer, getImportsForType(), namedSubTypes(), ObjectTypeExtensions, PhpInterfaceObjectTypeRenderer (+1 more)

### Community 5 - "Diff and Apply Checks"
Cohesion: 0.06
Nodes (7): Apply, Check, CheckOption, DiffConfiguration, Apply, RuleSet, DiffSetup

### Community 6 - "Bruno Renderers"
Cohesion: 0.07
Nodes (6): BrunoActionRenderer, BrunoMethodRenderer, BrunoRequestRenderer, BrunoUrl, InstanceSerializer, ObjectInstanceSerializer

### Community 7 - "Generator Components"
Cohesion: 0.07
Nodes (7): GeneratorComponent, OasGeneratorComponent, OasProvider, CoreCodeGenerator, executeAndMeasureTimeMillis(), toSeconds(), GenerateSubcommand

### Community 8 - "Method and Body Extensions"
Cohesion: 0.06
Nodes (20): EObjectExtensions, hasBody(), hasPathParams(), hasReturnPayload(), isSuccessfull(), resource(), returnType(), toParamName() (+12 more)

### Community 9 - "Postman Renderers"
Cohesion: 0.07
Nodes (9): ActionRenderer, auth(), authorization(), jScript(), testAuthScript(), MethodRenderer, PostmanModuleRenderer, PostmanUrl (+1 more)

### Community 10 - "Release Workflow"
Cohesion: 0.05
Nodes (50): if, name, needs, permissions, runs-on, steps, GITHUB_TOKEN, jobs (+42 more)

### Community 11 - "Data Sinks and API Providers"
Cohesion: 0.06
Nodes (5): ApiProvider, GeneratorModule, OasGeneratorModule, DataSink, FileDataSink

### Community 12 - "Vrap Object File Producers"
Cohesion: 0.07
Nodes (12): VrapObjectType, ApiRootFileProducer, IndexFileProducer, allMethods(), deprecated(), toJoiPackageName(), toJoiVrapType(), toRequestBuilderName() (+4 more)

### Community 13 - "Instance Serializers"
Cohesion: 0.09
Nodes (26): renderUriParameter(), toYaml(), getParent(), InstanceSerializer, isScalar(), normalize(), NullInstanceSerializer, ObjectInstanceSerializer (+18 more)

### Community 14 - "TypeScript CTP Client"
Cohesion: 0.06
Nodes (42): dependencies, @commercetools/sdk-client@^2.1.1, @commercetools/sdk-middleware-auth@^6.0.0, @commercetools/sdk-middleware-http@^6.0.0, joi@^14.3.1, node-fetch@^2.6.0, devDependencies, prettier@^1.18.2 (+34 more)

### Community 15 - "Validator Rule Tests"
Cohesion: 0.04
Nodes (47): ValidatorRulesTest, "asMap annotation rule", "boolean property name rule", "datetime rule", "discriminator name rule", "discriminator parent rule", "discriminator subtype rule", "discriminator value rule" (+39 more)

### Community 16 - "Code Generator Impl"
Cohesion: 0.09
Nodes (19): CodeGenerator, CodeGeneratorImpl, ComposedSchemaGenerator, MethodGenerator, NamedScalarTypeGenerator, NamedStringTypeGenerator, ObjectSchemaGenerator, ObjectTypeGenerator (+11 more)

### Community 17 - "RAML Markdown Lint"
Cohesion: 0.05
Nodes (38): IncludeYamlType, jsYamlSchema, markdownLintDefaults, prettifyRamlPath(), run(), validateMarkdown(), yamlLoad(), bin (+30 more)

### Community 18 - "PHP Request Builders"
Cohesion: 0.08
Nodes (7): getMethodName(), AbstractRequestBuilder, ClientConstants, forcedLiteralEscape(), toNamespaceName(), PhpMethodBuilderRenderer, PhpSubTemplates

### Community 21 - "CLI and Project Docs"
Cohesion: 0.05
Nodes (41): @commercetools-docs/rmf-codegen, Query Parameter Double Quote Fix, ramldoc-generator, 12.0.0 Release, rmf-codegen 0.18 Upgrade, rmf-codegen 0.1.10 Update, rmf-codegen 0.1.5 Update, rmf-codegen 0.1.7 Update (+33 more)

### Community 22 - "Deprecation Diff Checks"
Cohesion: 0.15
Nodes (29): CheckSeverity, ERROR, FATAL, INFO, WARN, DeprecatedAddedCheck, DeprecatedAddedMethodCheck, DeprecatedAddedPropertyCheck (+21 more)

### Community 23 - "Type Providers and Groovy DSL"
Cohesion: 0.09
Nodes (7): VrapTypeProvider, ExtensionsBase, GroovyDslRenderer, RamlExampleRenderer, RamlObjectTypeRenderer, PhpCollectionRenderer, RamlObjectTypeRenderer

### Community 24 - "Validator Rules"
Cohesion: 0.07
Nodes (7): Rule, RuleOption, RuleSeverity, ERROR, INFO, WARN, ValidatorFactory

### Community 25 - "Java Renderers"
Cohesion: 0.10
Nodes (6): JavaStringTypeRenderer, JavaCompleteModule, JavaInterfaceModelModule, JavaModelModule, JavaApiRootFileProducer, JavaModelClassFileProducer

### Community 26 - "Go Client Renderers"
Cohesion: 0.10
Nodes (7): ClientConstants, toStructName(), GoMethodRenderer, RequestBuilder, flattenVrapType(), goTypeName(), simpleGoName()

### Community 28 - "Vrap Type Model"
Cohesion: 0.10
Nodes (10): VrapArrayType, VrapEnumType, VrapNilType, VrapType, CsharpEObjectTypeExtensions, toCsharpVType(), NamedField, flattenVrapType() (+2 more)

### Community 29 - "Constants and Server Renderers"
Cohesion: 0.10
Nodes (4): VrapLibraryType, ConstantsProvider, ParameterGenerator, ServerRenderer

### Community 30 - "Diff Check Tests"
Cohesion: 0.06
Nodes (34): DiffCheckTest, "added enum", "added method", "added property", "added query parameter", "added resource", "added type", "deprecated method" (+26 more)

### Community 31 - "OAS Object Renderers"
Cohesion: 0.10
Nodes (16): format(), numberType(), OasObjectTypeRenderer, renderAny(), renderAnyType(), renderArray(), renderBoolean(), renderDateOnly() (+8 more)

### Community 32 - "Language Base Types"
Cohesion: 0.10
Nodes (13): LanguageBaseTypes, fromDefaultJavaType(), fromJavaType(), JavaBaseTypes, PlantUmlBaseTypes, primitiveType(), scalarType(), TestCodeGenerator (+5 more)

### Community 33 - "RAML Example Renderers"
Cohesion: 0.08
Nodes (12): allAnyTypes(), allResourceMethods(), toAnyTypeMap(), toInstanceMap(), toMethodMap(), toObjectTypeMap(), toParameterMap(), toPropertyMap() (+4 more)

### Community 34 - "CLI Diagnostic Printers"
Cohesion: 0.12
Nodes (15): LinkFormat, CLI, GITHUB, CliFormatPrinter, CliLinkFormatter, DiagnosticSeverity, ERROR, INFO (+7 more)

### Community 35 - "Joi Validator Renderers"
Cohesion: 0.13
Nodes (5): JoiObjectTypeExtensions, simpleJoiName(), toVrapType(), JoiValidatorModuleRenderer, PropertiesComparator

### Community 36 - "Vrap Extensions"
Cohesion: 0.10
Nodes (23): accessTokenUri(), accessTokenUriParams(), allParams(), apiResource(), firstBody(), fullClassName(), isScalar(), methodName() (+15 more)

### Community 38 - "C# Client Builder"
Cohesion: 0.08
Nodes (32): CsharpClientBuilderModule, CsharpHttpRequestRenderer, CsharpModelInterfaceRenderer, CsharpModule, CsharpObjectTypeRenderer, CsharpRequestBuilderRenderer, CsharpStringTypeRenderer, CsharpTraitRenderer (+24 more)

### Community 39 - "Go File Naming"
Cohesion: 0.09
Nodes (7): exportName(), goModelFileName(), goName(), snakeCase(), firstBody(), resourcePathList(), simpleTSName()

### Community 40 - "Go File Producers"
Cohesion: 0.13
Nodes (3): toBlockComment(), toLineComment(), GoFileProducer

### Community 42 - "TSLint Config"
Cohesion: 0.07
Nodes (29): rules, completed-docs, cyclomatic-complexity, increment-decrement, interface-name, linebreak-style, max-classes-per-file, member-access (+21 more)

### Community 43 - "Generator Targets and Plugin"
Cohesion: 0.11
Nodes (16): RamlGenerator, Target, TargetType, GO_CLIENT_COMPLETE, GROOVY_DSL, JAVA_API_BUILDER, JAVA_COMPLETE, JAVA_MODEL (+8 more)

### Community 44 - "Request Builder Renderers"
Cohesion: 0.13
Nodes (6): ResourceRenderer, RequestBuilderInit, ClientConstants, lowerCasePackage(), RequestBuilder, markDeprecated()

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
Cohesion: 0.12
Nodes (3): DiscriminatorValueRule, PackageDefinedRule, PolymorphicSubtypesRule

### Community 51 - "Scalar Type Mapping"
Cohesion: 0.12
Nodes (11): VrapScalarType, nativePostmanType(), fromCsharpType(), fromScalarCsharpType(), nativeGoType(), nativeOasType(), nativePostmanType(), nativePythonType() (+3 more)

### Community 52 - "CI Workflow"
Cohesion: 0.10
Nodes (23): jobs, test, test_node, name, on, permissions, continue-on-error, name (+15 more)

### Community 55 - "RAML Diagnostic Printers"
Cohesion: 0.15
Nodes (7): ConsoleDataSink, DefaultRamlDiagnosticPrinter, GithubRamlDiagnosticPrinter, Printer, GITHUB, LOGGER, RamlDiagnosticPrinter

### Community 56 - "Date Time and PHP Types"
Cohesion: 0.10
Nodes (11): DateTimeTypes, DateOnly, DateTime, TimeOnly, VrapAnyType, VrapDateTimeType, fromAnyType(), fromDateTimeType() (+3 more)

### Community 57 - "OAS Serializers"
Cohesion: 0.17
Nodes (6): ExampleSerializer, InlineTypeSerializer, OasModuleRenderer, ReferenceSerializer, RegExpSerializer, UriTemplateSerializer

### Community 58 - "RAML Doc Serializers"
Cohesion: 0.17
Nodes (6): ExampleSerializer, InlineTypeSerializer, RamldocModuleRenderer, ReferenceSerializer, RegExpSerializer, UriTemplateSerializer

### Community 59 - "Generation Targets"
Cohesion: 0.09
Nodes (23): GenerationTarget, BRUNO, CSHARP_CLIENT, CSHARP_QUERY_PREDICATES, CSHARP_TEST, DOC_MARKDOWN, GO_CLIENT, JAVA_CLIENT (+15 more)

### Community 61 - "Community 61"
Cohesion: 0.13
Nodes (8): getSuperTypes(), pyServiceClassName(), pyServiceModuleName(), toLineComment(), lowerCasePackage(), snakeCase(), toRelativePackageName(), toSchemaPackageName()

### Community 62 - "Community 62"
Cohesion: 0.17
Nodes (7): PropertyAddedCheck, PropertyRemovedCheck, Diff, DiffData, Position, Source, getSource()

### Community 63 - "Community 63"
Cohesion: 0.17
Nodes (17): getParent(), renderAnnotation(), renderArrayType(), renderBaseUriParameter(), renderEAttributes(), renderExample(), renderNumberType(), renderObjectType() (+9 more)

### Community 64 - "Community 64"
Cohesion: 0.09
Nodes (21): extends, options, rules, await-promise, deprecation, match-default-export-name, no-boolean-literal-compare, no-floating-promises (+13 more)

### Community 65 - "Community 65"
Cohesion: 0.18
Nodes (5): UnionTypeRenderer, EObjectTypeExtensions, PhpInterfaceUnionTypeRenderer, PhpUnionCollectionRenderer, PhpUnionTypeRenderer

### Community 66 - "Community 66"
Cohesion: 0.18
Nodes (3): sortedByTopology(), TopologySortContext, PythonSchemaRenderer

### Community 67 - "Community 67"
Cohesion: 0.14
Nodes (4): resource(), MethodResponseRule, ResolvedResourcesRule, UriParameterDeclaredRule

### Community 70 - "Community 70"
Cohesion: 0.16
Nodes (3): PackageProvider, ResourcePackageSwitch, TypePackageSwitch

### Community 71 - "Community 71"
Cohesion: 0.12
Nodes (17): EnumAddedCheck, EnumRemovedCheck, DiffDataType, ANY_TYPES, ANY_TYPES_MAP, API, METHOD_RESPONSE_TYPES_MAP, METHOD_TYPES_MAP (+9 more)

### Community 72 - "Community 72"
Cohesion: 0.15
Nodes (4): DiagnosticsAware, DiagnosticsCreator, ModulesRule, SdkBaseUriRule

### Community 76 - "Community 76"
Cohesion: 0.16
Nodes (3): BooleanPropertyNameRule, DomainTypeNameRule, TypesRule

### Community 77 - "Community 77"
Cohesion: 0.17
Nodes (10): csharpClassRelativePath(), csharpPackage(), firstBody(), pluralize(), requestBuildersPackage(), resourcePathList(), simpleName(), singularize() (+2 more)

### Community 78 - "Community 78"
Cohesion: 0.15
Nodes (7): main(), RMFCommand, LogLevel, DEBUG, ERROR, INFO, WARN

### Community 79 - "Community 79"
Cohesion: 0.15
Nodes (8): description(), escapeJson(), getParent(), oAuth2(), description(), escapeJson(), getParent(), oAuth2()

### Community 83 - "Community 83"
Cohesion: 0.18
Nodes (3): ResourcesRule, SuccessBodyRule, Validator

### Community 84 - "Community 84"
Cohesion: 0.16
Nodes (3): toHtml(), filterLinks(), toHtml()

### Community 85 - "Community 85"
Cohesion: 0.20
Nodes (11): generateTemplate(), IndentationTokens, IndentMarker, DoubleAngle, DoubleCurly, SingleAngle, SingleCurly, indentString() (+3 more)

### Community 88 - "Community 88"
Cohesion: 0.25
Nodes (5): NamedScalarTypeRenderer, NamedStringTypeRenderer, RamlAnyTypeRenderer, RamlScalarTypeRenderer, RamlStringTypeRenderer

### Community 91 - "Community 91"
Cohesion: 0.16
Nodes (7): firstBody(), fullClassName(), isExpandable(), resourcePathList(), simpleName(), toJavaPackage(), toJavaVType()

### Community 95 - "Community 95"
Cohesion: 0.22
Nodes (3): InternalLogger, safeRun(), VerifySubcommand

### Community 100 - "Community 100"
Cohesion: 0.14
Nodes (13): autoload, psr-4, description, license, name, Commercetools\\, Commercetools\\Test\\, repositories (+5 more)

### Community 101 - "Community 101"
Cohesion: 0.25
Nodes (3): RequestBuilder, pyTypeName(), simplePyName()

### Community 102 - "Community 102"
Cohesion: 0.23
Nodes (4): PatternStringTypeRenderer, packageDir(), OasScalarTypeRenderer, RamlScalarTypeRenderer

### Community 108 - "Community 108"
Cohesion: 0.33
Nodes (7): DeprecatedRemovedPropertyCheck, MarkDeprecatedAddedPropertyCheck, MarkDeprecatedRemovedPropertyCheck, PropertyOptionalCheck, PropertyRequiredCheck, PropertyTypeChangedCheck, PropertyReference

### Community 112 - "Community 112"
Cohesion: 0.35
Nodes (3): toJson(), InstanceSerializer, ObjectInstanceSerializer

### Community 118 - "Community 118"
Cohesion: 0.27
Nodes (3): ValidatorFixtures, ValidatorSetupTest, ValidatorSetupTest

### Community 124 - "Community 124"
Cohesion: 0.31
Nodes (5): DataProvider, MethodBodyTypeChangedCheck, MethodResponseBodyTypeChangedCheck, MethodBodyTypeReference, MethodResponseBodyTypeReference

### Community 125 - "Community 125"
Cohesion: 0.36
Nodes (5): DiffCheck, MethodAddedCheck, MethodRemovedCheck, QueryParameterAddedCheck, QueryParameterRemovedCheck

### Community 126 - "Community 126"
Cohesion: 0.33
Nodes (3): Differ, Builder, RamlDiff

### Community 127 - "Community 127"
Cohesion: 0.20
Nodes (10): Scope, ENUM, METHOD, METHOD_BODY, METHOD_RESPONSE_BODY, PROPERTY, QUERY_PARAMETER, RESOURCE (+2 more)

### Community 134 - "Community 134"
Cohesion: 0.27
Nodes (3): JavaEObjectTypeExtensions, ExtensionsBase, JavaStringTypeRenderer

### Community 140 - "Community 140"
Cohesion: 0.33
Nodes (3): BaseTest, DiffSetupTest, "default set"

### Community 142 - "Community 142"
Cohesion: 0.28
Nodes (8): BooleanPropertyNameRule, commercetools-docs Validator Rules Page, CTP Validator Rule Creator Agent, Test RAML Files, TypesRule, TypesSwitch Case Methods, ValidatorFactory, @ValidatorSet

### Community 144 - "Community 144"
Cohesion: 0.22
Nodes (9): OutputFormat, CLI, DOTNET_MARKDOWN, GITHUB, JAVA_MARKDOWN, JSON, MARKDOWN, PHP_MARKDOWN (+1 more)

### Community 151 - "Community 151"
Cohesion: 0.25
Nodes (7): before_cache, cache, directories, jdk, language, script, services

### Community 153 - "Community 153"
Cohesion: 0.29
Nodes (7): DiffType, ADDED, CHANGED, DEPRECATED, MARK_DEPRECATED, REMOVED, REQUIRED

### Community 166 - "Community 166"
Cohesion: 0.38
Nodes (6): ctp-validators Ruleset, Guide to Update CTP Validators, ResourcePluralRule, Rule Description Page, Validator Rules Folder, Validator Test Examples

### Community 167 - "Community 167"
Cohesion: 0.29
Nodes (6): DiffLanguagesCheckTest, "added method", "added method get category dotnet", "added method get category java", "added method get category php", "added method get category ts"

### Community 168 - "Community 168"
Cohesion: 0.29
Nodes (6): interval, updates, directory, package-ecosystem, schedule, version

### Community 171 - "Community 171"
Cohesion: 0.29
Nodes (6): compilerOptions, module, outDir, target, exclude, include

### Community 192 - "Community 192"
Cohesion: 0.40
Nodes (4): extends, tslint-config-prettier, tslint-plugin-prettier, tslint:all

### Community 200 - "Community 200"
Cohesion: 0.83
Nodes (3): installVrapCli(), install.sh script, uninstallVrapCli()

### Community 201 - "Community 201"
Cohesion: 0.83
Nodes (3): installRmfCli(), install_local.sh script, uninstallRmfCli()

### Community 203 - "Community 203"
Cohesion: 0.67
Nodes (3): options, order, member-ordering

## Ambiguous Edges - Review These
- `JAVA_CLIENT` → `Gradle Target Values`  [AMBIGUOUS]
  tools/codegen-gradle-plugin/README.md · relation: conceptually_related_to
- `TYPESCRIPT_CLIENT` → `Gradle Target Values`  [AMBIGUOUS]
  tools/codegen-gradle-plugin/README.md · relation: conceptually_related_to
- `rmf-codegen 0.18 Upgrade` → `rmf-codegen 0.1.10 Update`  [AMBIGUOUS]
  node/rmf-codegen/CHANGELOG.md · relation: conceptually_related_to

## Knowledge Gaps
- **225 isolated node(s):** `SingleAngle`, `DoubleAngle`, `SingleCurly`, `DoubleCurly`, `DateTime` (+220 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 881 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **193 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `TemplateFile` connect `File Producers and Templates` to `Renderer Base Types`, `Trait and Object Renderers`, `Bruno Renderers`, `Generator Components`, `Method and Body Extensions`, `Postman Renderers`, `Data Sinks and API Providers`, `Vrap Object File Producers`, `Code Generator Impl`, `PHP Request Builders`, `OAS Renderers`, `OAS Path Renderers`, `Type Providers and Groovy DSL`, `Java Renderers`, `Go Client Renderers`, `PlantUML Diagrams`, `Constants and Server Renderers`, `OAS Object Renderers`, `RAML Example Renderers`, `Joi Validator Renderers`, `PHP Object Renderers`, `Go File Naming`, `Go File Producers`, `Markdown Renderers`, `Request Builder Renderers`, `OAS Type Renderers`, `Java HTTP Renderers`, `Scalar Type Mapping`, `RAML Resource Renderers`, `Java String HTTP Renderers`, `RAML Diagnostic Printers`, `OAS Serializers`, `RAML Doc Serializers`, `Community 61`, `Community 63`, `Community 65`, `Community 66`, `Community 68`, `Community 69`, `Community 73`, `Community 74`, `Community 80`, `Community 81`, `Community 82`, `Community 86`, `Community 88`, `Community 90`, `Community 91`, `Community 92`, `Community 94`, `Community 96`, `Community 98`, `Community 99`, `Community 101`, `Community 102`, `Community 103`, `Community 105`, `Community 106`, `Community 109`, `Community 110`, `Community 111`, `Community 119`, `Community 121`, `Community 130`, `Community 131`, `Community 132`, `Community 133`, `Community 134`, `Community 143`, `Community 148`, `Community 149`, `Community 150`, `Community 182`, `Community 183`, `Community 185`, `Community 188`, `Community 189`, `Community 190`, `Community 191`, `Community 198`, `Community 202`, `Community 204`?**
  _High betweenness centrality (0.164) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `VrapTypeProvider` (e.g. with `AnyTypeProvider` and `ResourcesTypeProvider`) actually correct?**
  _`VrapTypeProvider` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `SingleAngle`, `DoubleAngle`, `SingleCurly` to the rest of the system?**
  _225 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Generator Core Config` be split into smaller, more focused modules?**
  _Cohesion score 0.06056701030927835 - nodes in this community are weakly interconnected._
- **Why does `VrapTypeProvider` connect `Type Providers and Groovy DSL` to `Community 129`, `Renderer Base Types`, `Trait and Object Renderers`, `Community 130`, `Community 132`, `Bruno Renderers`, `Community 133`, `Method and Body Extensions`, `Community 137`, `Community 134`, `Postman Renderers`, `Vrap Object File Producers`, `Community 143`, `PHP Request Builders`, `OAS Renderers`, `Community 148`, `Community 150`, `Java Renderers`, `Go Client Renderers`, `PlantUML Diagrams`, `Vrap Type Model`, `Constants and Server Renderers`, `OAS Object Renderers`, `Language Base Types`, `RAML Example Renderers`, `Joi Validator Renderers`, `PHP Object Renderers`, `Go File Naming`, `Go File Producers`, `Markdown Renderers`, `Request Builder Renderers`, `Java HTTP Renderers`, `Any Type Provider`, `RAML Resource Renderers`, `Java String HTTP Renderers`, `OAS Serializers`, `Community 186`, `RAML Doc Serializers`, `Community 61`, `Community 190`, `Community 63`, `Community 65`, `Community 194`, `Community 66`, `Community 68`, `Community 69`, `Community 73`, `Community 74`, `Community 80`, `Community 82`, `Community 86`, `Community 88`, `Community 90`, `Community 92`, `Community 94`, `Community 96`, `Community 98`, `Community 101`, `Community 102`, `Community 103`, `Community 105`, `Community 106`, `Community 110`, `Community 111`, `Community 119`, `Community 121`, `Community 123`?**
  _High betweenness centrality (0.122) - this node is a cross-community bridge._

### Low-confidence Hints
_AMBIGUOUS edges — the extractor was unsure. Verify before acting on these._

- **What is the exact relationship between `JAVA_CLIENT` and `Gradle Target Values`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `TYPESCRIPT_CLIENT` and `Gradle Target Values`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._