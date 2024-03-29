package io.vrap.codegen.languages.javalang.dsl;

import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.JavaEObjectTypeExtensions
import io.vrap.codegen.languages.java.base.extensions.JavaObjectTypeExtensions
import io.vrap.codegen.languages.java.base.extensions.toJavaVType
import io.vrap.rmf.codegen.firstLowerCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType

class GroovyDslRenderer constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val content = """
            |package ${vrapType.`package`};
            |
            |import groovy.lang.Closure;
            |import groovy.lang.DelegatesTo;
            |import io.vrap.rmf.base.client.utils.Generated;
            |
            |/**
            | * Provides a Groovy DSL to build instances of this type.
            | */
            |${JavaSubTemplates.generatedAnnotation}
            |public interface ${vrapType.simpleClassName}Dsl {
            |  /**
            |   * Create a new instance of this type.
            |   *
            |   * @param closure the closure to initialize the fields of the new instance
            |   * @return new instance intialized via the given closure
            |   */
            |  default ${vrapType.simpleClassName} ${type.name.firstLowerCase()}(@DelegatesTo(value = ${vrapType.simpleClassName}.class, strategy = Closure.DELEGATE_FIRST) final Closure<${vrapType.simpleClassName}> closure) {
            |    final ${vrapType.simpleClassName} ${type.name.firstLowerCase()} = new ${vrapType.simpleClassName}();
            |    closure.setDelegate(${type.name.firstLowerCase()});
            |    closure.setResolveStrategy(Closure.DELEGATE_FIRST);
            |    closure.call();
            |    return ${type.name.firstLowerCase()};
            |  }
            |}
        """.trimMargin()


        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + "Dsl.java",
                content = content
        )
    }
}
