package io.vrap.rmf.codegen.common.generator.extensions.types;

import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.types.StringInstance;
import io.vrap.rmf.raml.model.types.StringType;

import java.util.List;
import java.util.stream.Collectors;

@ModelExtension(extend = StringType.class)
public class StringTypeExtension {


    @ExtensionMethod
    boolean isEnumeration(StringType stringType){
        return stringType.getEnum()==null || stringType.getEnum().size() == 0;
    }

    @ExtensionMethod
    List<String> getEnumJsonNames(StringType stringType){
        List<String> enumJsonNames = stringType.getEnum()
                .stream()
                .map(StringInstance.class::cast)
                .map(StringInstance::getValue)
                .collect(Collectors.toList());
        return enumJsonNames;
    }
}
