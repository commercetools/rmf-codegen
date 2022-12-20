package com.commercetools.rmf.diff

class DiffLanguagesCheckTest extends BaseTest {
    def defaultSeverity = CheckSeverity.INFO

    def "added method"() {
        when:
        def printer = new JavaMarkdownFormatPrinter()
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
        def printer = new JavaMarkdownFormatPrinter()
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
        def printer = new PHPMarkdownFormatPrinter()
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
        def printer = new TSMarkdownFormatPrinter()
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
        def printer = new DotNetMarkdownFormatPrinter()
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
