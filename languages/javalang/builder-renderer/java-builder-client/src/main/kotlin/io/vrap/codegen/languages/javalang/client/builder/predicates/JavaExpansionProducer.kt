package io.vrap.codegen.languages.javalang.client.builder.predicates

import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.toJavaPackage
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent

class JavaExpansionProducer constructor(val basePackageName: String) : FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                generateCollectionHelper(),
                expansionDsl()
        )
    }

    private fun expansionDsl() : TemplateFile {
        val content =  """
            |package ${basePackageName.toJavaPackage()}.predicates.expansion;
            |
            |import io.vrap.rmf.base.client.Builder;
            |import io.vrap.rmf.base.client.utils.Generated;
            |import java.util.List;
            |
            |<<${JavaSubTemplates.generatedAnnotation}>>
            |public interface ExpansionDsl extends Builder<String> {
            |    public List<String> getPath();
            |    
            |    @Override
            |    public default String build() {
            |        return String.join(".", getPath());
            |    }
            |}
        """.trimMargin().keepAngleIndent()

        return TemplateFile(
                relativePath = "$basePackageName.predicates.expansion.ExpansionDsl".replace(".", "/") + ".java",
                content = content
        )
    }
    private fun generateCollectionHelper() : TemplateFile {
        val content =  """
            |package ${basePackageName.toJavaPackage()}.predicates.expansion;
            |
            |import io.vrap.rmf.base.client.utils.Generated;
            |import java.util.ArrayList;
            |import java.util.Collections;
            |import java.util.List;
            |
            |<<${JavaSubTemplates.generatedAnnotation}>>
            |public class ExpansionUtil {
            |
            |    public static <T> List<T> appendOne(final List<T> list, T element) {
            |        final List<T> arr = new ArrayList(list);
            |        arr.add(element);
            |        return Collections.unmodifiableList(arr);
            |    }
            |}
        """.trimMargin().keepAngleIndent()

        return TemplateFile(
                relativePath = "$basePackageName.predicates.expansion.ExpansionUtil".replace(".", "/") + ".java",
                content = content
        )
    }
}
