package io.vrap.rmf.codegen.common.generator.doc;

import io.vrap.rmf.raml.model.types.DescriptionFacet;
import io.vrap.rmf.raml.model.types.StringInstance;

import java.util.Optional;


public abstract class JavaDocProcessor {

    protected abstract Optional<String> markDownToJavaDoc(String markdownDescription);

    public final String markDownToJavaDoc(DescriptionFacet descriptionFacet) {
        return Optional.ofNullable(descriptionFacet)
                .map(DescriptionFacet::getDescription)
                .map(StringInstance::getValue)
                .flatMap(this::markDownToJavaDoc)
                .orElse("");
    }


}
