package io.vrap.rmf.codegen.cli

import com.google.common.net.MediaType
import io.vrap.rmf.codegen.cli.diff.CheckSeverity
import io.vrap.rmf.codegen.cli.diff.Diff
import io.vrap.rmf.codegen.cli.diff.Scope
import io.vrap.rmf.codegen.firstUpperCase


class DiffLanguagesSubcommand : DiffSubcommand() {
    class JavaMarkdownFormatPrinter: FormatPrinter {

        fun replaceMessage(diff: Diff<Any>): Diff<Any> {
            val values: Array<String> = diff.value.toString().split(" /").toTypedArray()
            val message = "${diff.diffType.toString().lowercase()} ${diff.scope.toString().lowercase()} `apiRoot.withProjectKey(\"\").${values[1]}().${values[0]}()`"
            return when (diff.scope) {
                Scope.METHOD -> Diff(diff.diffType, diff.scope, diff.value, message, diff.eObject, diff.severity, diff.diffEObject, diff.source)
                else -> diff
            }
        }
        override fun print(diffResult: List<Diff<Any>>): String {

            val map = diffResult.map { replaceMessage(it) }.groupBy { it.scope }.map { it.key to it.value.groupBy { it.diffType } }.toMap()


            return map.entries.joinToString("\n\n") { scope -> """
                |${scope.value.entries.joinToString("\n\n") { type -> """
                    |<details>
                    |<summary>${type.key.type.firstUpperCase()} ${scope.key.scope.firstUpperCase()}(s)</summary>
                    |
                |${type.value.joinToString("\n") { "- ${it.severity.asSign()}${it.message}" }}
                |</details>
                        |
                        """.trimMargin() }}
                    """.trimMargin() }
        }

        fun CheckSeverity.asSign(): String {
            return when(this) {
                CheckSeverity.FATAL -> ":red_circle: "
                CheckSeverity.ERROR -> ":warning: "
                CheckSeverity.WARN -> ":warning: "
                else -> ""

            }
        }
    }

    class PHPMarkdownFormatPrinter: FormatPrinter {

        private fun messageTemplate(diff: Diff<Any>, values: Array<String>) :String {
            return """${diff.diffType.toString().lowercase()} ${diff.scope.toString().lowercase()} `${"$"}apiRoot->${values[1]}()->${values[0]}()`"""
        }
        fun replaceMessage(diff: Diff<Any>): Diff<Any> {
            val values: Array<String> = diff.value.toString().split(" /").toTypedArray()

            return when (diff.scope) {
                Scope.METHOD -> Diff(diff.diffType, diff.scope, diff.value, messageTemplate(diff, values), diff.eObject, diff.severity, diff.diffEObject, diff.source)
                else -> diff
            }
        }
        override fun print(diffResult: List<Diff<Any>>): String {

            val map = diffResult.map { replaceMessage(it) }.groupBy { it.scope }.map { it.key to it.value.groupBy { it.diffType } }.toMap()


            return map.entries.joinToString("\n\n") { scope -> """
                |${scope.value.entries.joinToString("\n\n") { type -> """
                    |<details>
                    |<summary>${type.key.type.firstUpperCase()} ${scope.key.scope.firstUpperCase()}(s)</summary>
                    |
                |${type.value.joinToString("\n") { "- ${it.severity.asSign()}${it.message}" }}
                |</details>
                        |
                        """.trimMargin() }}
                    """.trimMargin() }
        }

        fun CheckSeverity.asSign(): String {
            return when(this) {
                CheckSeverity.FATAL -> ":red_circle: "
                CheckSeverity.ERROR -> ":warning: "
                CheckSeverity.WARN -> ":warning: "
                else -> ""

            }
        }
    }
}
