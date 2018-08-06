package io.vrap.rmf.codegen.common.generator.extensions;

import com.google.inject.Inject;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.types.DescriptionFacet;
import io.vrap.rmf.raml.model.types.Property;
import io.vrap.rmf.raml.model.types.StringInstance;
import io.vrap.rmf.raml.model.types.StringType;

import java.util.List;
import java.util.stream.Collectors;

@ModelExtension(extend = Property.class)
public class PropertyExtension {

    private DescriptionFacetExtension descriptionFacetExtension;

    @Inject
    public void setDescriptionFacetExtension(final DescriptionFacetExtension descriptionFacetExtension) {
        this.descriptionFacetExtension = descriptionFacetExtension;
    }

    @ExtensionMethod
    public Boolean  isPatternProperty(Property property){
        return property.getName().startsWith("/");
    }

    @ExtensionMethod
    public Boolean hasDescription(final Property property) {
        return property.getType().isInlineType() && descriptionFacetExtension.hasDescription(property.getType());
    }

    @ExtensionMethod
    public String getDescription(final Property property) {
        return descriptionFacetExtension.getDescription(property.getType());
    }


}
