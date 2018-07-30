package io.vrap.rmf.codegen.common.generator.extensions.types;

import com.google.inject.Inject;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.types.DescriptionFacet;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@ModelExtension(extend = DescriptionFacet.class)
public class DescriptionFacetExtension {

    private JavaDocProcessor javaDocProcessor;

    @Inject
    public void setJavaDocProcessor(JavaDocProcessor javaDocProcessor) {
        this.javaDocProcessor = javaDocProcessor;
    }

    @ExtensionMethod
    public String getDescription(DescriptionFacet descriptionFacet) {
        return javaDocProcessor.markDownToJavaDoc(descriptionFacet);
    }
}
