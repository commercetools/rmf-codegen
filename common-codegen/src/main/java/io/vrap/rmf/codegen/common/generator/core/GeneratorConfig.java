package io.vrap.rmf.codegen.common.generator.core;

import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import org.eclipse.emf.common.util.URI;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.Map;

@Value.Immutable
@Value.Style(
        visibility = Value.Style.ImplementationVisibility.PRIVATE,
        builderVisibility = Value.Style.BuilderVisibility.PUBLIC
)
public interface GeneratorConfig {

    String PACKAGE_PREFIX = "PACKAGE_PREFIX";

    String OUTPUT_FOLDER = "OUTPUT_FOLDER";

    String RAML_FILE_LOCATION = "RAML_FILE_LOCATION";


    String getPackagePrefix();

    Path getOutputFolder();

    URI getRamlFileLocation();

    JavaDocProcessor getJavaDocProcessor();

    Map<String, String> getCustomTypeMapping();


}
