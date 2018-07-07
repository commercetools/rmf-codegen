package io.vrap.rmf.codegen.common.generator.core;

import io.vrap.rmf.codegen.common.generator.doc.DefaultJavaDocProcessor;
import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import org.eclipse.emf.common.util.URI;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
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



    @Value.Default
    default String getPackagePrefix(){
        return "io.vrap.rmf";
    }

    @Value.Default
    default Path getOutputFolder(){
        return Paths.get("build/generated/src");
    }

    @Value.Default
    default JavaDocProcessor getJavaDocProcessor(){
        return new DefaultJavaDocProcessor();
    }

    @Value.Default
    default Map<String, String> getCustomTypeMapping(){
        return Collections.EMPTY_MAP;
    }


    URI getRamlFileLocation();

}
