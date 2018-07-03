package io.vrap.rmf.codegen.common.generator.model.codegen.base;

import com.squareup.javapoet.*;
import io.vrap.rmf.codegen.common.generator.core.ConfigDecoratorBase;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.util.CodeGeneratorUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.lang.model.element.Modifier;

public class BaseClass extends ConfigDecoratorBase {

    private final ClassName className;
    private final TypeSpec typeSpec;

    public BaseClass(GeneratorConfig delegate) {
        super(delegate);
        this.className = ClassName.get(getPackagePrefix() + ".base", getSimpleClassName());
        this.typeSpec = createTypeSpec();
    }

    public final ClassName getClassName() {
        return className;
    }

    public final TypeSpec getTypeSpec() {
        return typeSpec;
    }

    protected String getSimpleClassName() {
        return "Base";
    }

    protected TypeSpec createTypeSpec() {

        final MethodSpec toString = MethodSpec.methodBuilder("toString")
                .addCode("return $T.reflectionToString(this, $T.SHORT_PREFIX_STYLE);\n", ToStringBuilder.class, ToStringStyle.class)
                .addAnnotation(Override.class)
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .build();

        final MethodSpec equals = MethodSpec.methodBuilder("equals")
                .addParameter(ParameterSpec.builder(TypeName.get(Object.class), "o").build())
                .addCode("return $T.reflectionEquals(this, o);\n", EqualsBuilder.class)
                .addAnnotation(Override.class)
                .returns(TypeName.BOOLEAN.unbox())
                .addModifiers(Modifier.PUBLIC)
                .build();

        final MethodSpec hashCode = MethodSpec.methodBuilder("hashCode")
                .addCode("return $T.reflectionHashCode(this);\n", HashCodeBuilder.class)
                .addAnnotation(Override.class)
                .returns(TypeName.INT.unbox())
                .addModifiers(Modifier.PUBLIC)
                .build();

        final TypeSpec typeSpec = TypeSpec.classBuilder(getClassName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(CodeGeneratorUtil.getGeneratedAnnotation(this))
                .addMethod(toString)
                .addMethod(equals)
                .addMethod(hashCode)
                .build();

        return typeSpec;

    }
}
