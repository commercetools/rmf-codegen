package io.vrap.rmf.codegen.common;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfigBuilder;
import io.vrap.rmf.codegen.common.generator.core.STCodeGenerator;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorComponent;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorModule;
import org.eclipse.emf.common.util.URI;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CodeGeneratorTest {


    private GeneratorConfig generatorConfig;
    private GeneratorComponent generatorComponent;
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Before
    public void init() {

        Map<String, String> customTypeMapping = new HashMap<>();
        {
            customTypeMapping.put("CountryCode", CountryCode.class.getCanonicalName());
            customTypeMapping.put("CurrencyCode", CurrencyCode.class.getCanonicalName());
        }

        final URI ramlFileLocation = URI.createURI("../api-spec/import-storage-api.raml");
        final String gensrc = System.getProperty("GENSRC");
        final String current = System.getProperty("user.dir");
        final Path outputPath = gensrc == null ?
                Paths.get(current, "build/gensrc") :
                Paths.get(gensrc);
        generatorConfig = new GeneratorConfigBuilder()
                .packagePrefix("com.commercetools.importapi")
                .outputFolder(outputPath)
                .ramlFileLocation(ramlFileLocation)
                .customTypeMapping(customTypeMapping)
                .build();

        generatorComponent = new GeneratorComponent(GeneratorModule.of(generatorConfig));
    }


    @Test
    public void testTemplate() throws Exception {
        STCodeGenerator stCodeGenerator = generatorComponent.getStCodeGenerator();
        stCodeGenerator.generateClasses()
                .subscribe(generationResult -> System.out.println(generationResult), Throwable::printStackTrace);
    }


}
