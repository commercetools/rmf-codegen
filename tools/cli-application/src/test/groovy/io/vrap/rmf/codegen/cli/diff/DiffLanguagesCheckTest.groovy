package io.vrap.rmf.codegen.cli.diff

import io.vrap.rmf.codegen.cli.DiffSubcommand

class DiffLanguagesCheckTest extends DiffCheckTest {
    def defaultSeverity = CheckSeverity.INFO

    def "added method"() {
        when:
        def printer = new DiffSubcommand.JavaMarkdownFormatPrinter()
        def check = diff("/method-added", new MethodAddedCheck(defaultSeverity))
        def results = printer.print(check.diff())
        then:
        results == """<details>
            |<summary>Added Method(s)</summary>
            |
            |- added method `apiRoot.foo().get()`
            |- added method `apiRoot.bar().get()`
            |</details>
            |""".stripMargin()
    }

    def "added method get category java"() {
        when:
        def printer = new DiffSubcommand.JavaMarkdownFormatPrinter()
        def check = diff("/method-added-get-category", new MethodAddedCheck(defaultSeverity))
        def results = printer.print(check.diff())
        then:
        results == """<details>
            |<summary>Added Method(s)</summary>
            |
            |- added method `apiRoot.withProjectKeyValue().categories().get()`
            |</details>
            |""".stripMargin()
    }

    def "added method get category php"() {
        when:
        def printer = new DiffSubcommand.PHPMarkdownFormatPrinter()
        def check = diff("/method-added-get-category", new MethodAddedCheck(defaultSeverity))
        def results = printer.print(check.diff())
        then:
        results == """<details>
            |<summary>Added Method(s)</summary>
            |
            |- added method `${'$'}apiRoot->withProjectKeyValue()->categories()->get()`
            |</details>
            |""".stripMargin()
    }

    def "added method get category ts"() {
        when:
        def printer = new DiffSubcommand.TSMarkdownFormatPrinter()
        def check = diff("/method-added-get-category", new MethodAddedCheck(defaultSeverity))
        def results = printer.print(check.diff())
        then:
        results == """<details>
            |<summary>Added Method(s)</summary>
            |
            |- added method `apiRoot.withProjectKeyValue().categories().get()`
            |</details>
            |""".stripMargin()
    }

    def "added method get category dotnet"() {
        when:
        def printer = new DiffSubcommand.DotNetMarkdownFormatPrinter()
        def check = diff("/method-added-get-category", new MethodAddedCheck(defaultSeverity))
        def results = printer.print(check.diff())
        then:
        results == """<details>
            |<summary>Added Method(s)</summary>
            |
            |- added method `apiRoot.withProjectKeyValue().categories().get()`
            |</details>
            |""".stripMargin()
    }
}
