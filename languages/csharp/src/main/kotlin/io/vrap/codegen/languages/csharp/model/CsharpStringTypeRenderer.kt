//import io.vrap.codegen.languages.csharp.extensions.NAMESPACE_SUFFIX
//import io.vrap.codegen.languages.csharp.extensions.enumValueName

package io.vrap.codegen.languages.csharp.model

import com.google.inject.Inject
import io.vrap.codegen.languages.csharp.extensions.CsharpObjectTypeExtensions
import io.vrap.codegen.languages.csharp.extensions.NAMESPACE_SUFFIX
import io.vrap.codegen.languages.csharp.extensions.enumValueName
import io.vrap.codegen.languages.csharp.extensions.toCsharpVType
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.StringTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.types.StringType

class CsharpStringTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : CsharpObjectTypeExtensions, StringTypeRenderer {

    override fun render(type: StringType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toCsharpVType() as VrapEnumType

        val content = """
                |using System.ComponentModel;
                |
                |namespace ${vrapType.`package`}$NAMESPACE_SUFFIX
                |{
                |   public enum ${vrapType.simpleClassName}
                |   {
                |       <${type.enumFields()}>
                |   }
                |}
                """.trimMargin().keepIndentation()


        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".cs",
                content = content
        )
    }

    fun StringType.enumFields() = enumValues()
            ?.map {
                """
                 |[Description("${it.value}")]
                 |${it.value.enumValueName()}
            """.trimMargin()
            }
            ?.joinToString(separator = ",\n\n", postfix = "")

    fun StringType.enumValues() =  enum?.filter { it is StringInstance }
            ?.map { it as StringInstance }
            ?.filter { it.value != null }
}
