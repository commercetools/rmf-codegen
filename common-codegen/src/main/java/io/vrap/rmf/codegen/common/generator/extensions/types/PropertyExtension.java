package io.vrap.rmf.codegen.common.generator.extensions.types;

import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.types.Property;
import io.vrap.rmf.raml.model.types.StringInstance;
import io.vrap.rmf.raml.model.types.StringType;

import java.util.List;
import java.util.stream.Collectors;

@ModelExtension(extend = Property.class)
public class PropertyExtension {


    @ExtensionMethod
    public Boolean  isPatternProperty(Property property){
        return property.getName().startsWith("/");
    }


}
