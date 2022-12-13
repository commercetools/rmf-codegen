package io.vrap.rmf.codegen.cli.diff

import io.vrap.rmf.codegen.cli.DiffLanguagesSubcommand

class DiffLanguagesCheckTest extends DiffCheckTest {
    def defaultSeverity = CheckSeverity.INFO

    def "added method"() {
        when:
        def printer = new DiffLanguagesSubcommand.JavaMarkdownFormatPrinter()
        def check = diff("/method-added", new MethodAddedCheck(defaultSeverity))
        def results = check.diff().collect { printer.replaceMessage(it) }
        then:
        results.size() == 2
        results[0].message == "added method `apiRoot.withProjectKey(\"\").foo().get()`"
        results[1].message == "added method `apiRoot.withProjectKey(\"\").bar().get()`"
    }

    def "added method get category"() {
        when:
        def printer = new DiffLanguagesSubcommand.JavaMarkdownFormatPrinter()
        def check = diff("/method-added-get-category", new MethodAddedCheck(defaultSeverity))
        def results = check.diff().collect { printer.replaceMessage(it) }
        then:
        results.size() == 1
        results[0].message == "added method `apiRoot.withProjectKey(\"\").categories().get()`"
    }

    def "added method get category"() {
        when:
        def printer = new DiffLanguagesSubcommand.PHPMarkdownFormatPrinter()
        def check = diff("/method-added-get-category", new MethodAddedCheck(defaultSeverity))
        def results = check.diff().collect { printer.replaceMessage(it) }
        then:
        results.size() == 1
        results[0].message == "added method `\$apiRoot->categories()->get()`"
    }
}
