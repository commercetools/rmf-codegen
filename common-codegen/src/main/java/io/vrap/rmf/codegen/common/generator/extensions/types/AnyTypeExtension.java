package io.vrap.rmf.codegen.common.generator.extensions.types;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.types.AnyType;

import javax.inject.Inject;

@ModelExtension(extend = AnyType.class)
public class AnyTypeExtension {

    GeneratorConfig generatorConfig;

    @Inject
    public void setGeneratorConfig(final GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    @ExtensionMethod
    public TypeName getTypeName(final AnyType anyType) {
        return TypeNameSwitch.of(generatorConfig).doSwitch(anyType);
    }

    @ExtensionMethod
    public String getPackageName(final AnyType anyType){
        return ((ClassName)getTypeName(anyType)).packageName();
    }

    @ExtensionMethod
    public String getSimpleClassName(final AnyType anyType){
        return ((ClassName)getTypeName(anyType)).simpleName();
    }

}
