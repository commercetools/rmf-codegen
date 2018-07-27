package io.vrap.rmf.codegen.common.generator.extensions.types;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.vrap.functional.utils.TypeSwitch;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.types.AnyType;

import javax.inject.Inject;
import java.lang.reflect.Type;

@ModelExtension(extend = AnyType.class)
public class AnyTypeExtension {

    TypeNameSwitch typeNameSwitch;

    @Inject
    public void setGeneratorConfig(final GeneratorConfig generatorConfig) {
        this.typeNameSwitch = TypeNameSwitch.of(generatorConfig);
    }

    @ExtensionMethod
    public TypeName getTypeName(final AnyType anyType) {
        return typeNameSwitch.doSwitch(anyType);
    }

    @ExtensionMethod
    public String getPackageName(final AnyType anyType) {
        return ((ClassName) getTypeName(anyType)).packageName();
    }

    @ExtensionMethod
    public String getSimpleClassName(final AnyType anyType) {

        TypeName typeName = getTypeName(anyType);
        if (typeName instanceof ClassName) {
            return ((ClassName) typeName).simpleName();
        }
        else if (typeName instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) typeName).toString();
        }
        return typeName.toString();
    }
}
