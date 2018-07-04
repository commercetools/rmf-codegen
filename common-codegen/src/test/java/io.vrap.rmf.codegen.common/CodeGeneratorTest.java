package io.vrap.rmf.codegen.common;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import io.reactivex.observers.TestObserver;
import io.vrap.rmf.codegen.common.generator.MasterCodeGenerator;
import io.vrap.rmf.codegen.common.generator.client.spring.SpringClientCodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfigBuilder;
import io.vrap.rmf.codegen.common.generator.doc.DefaultJavaDocProcessor;
import io.vrap.rmf.codegen.common.generator.injection.DaggerGeneratorComponent;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorComponent;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorModule;
import io.vrap.rmf.codegen.common.generator.model.codegen.BeanGenerator;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CodeGeneratorTest {


    private GeneratorConfig generatorConfig;
    final Logger logger = LoggerFactory.getLogger(getClass());


    @Before
    public void init() {

        Map<String, String> customTypeMapping = new HashMap<>();
        {
            customTypeMapping.put("CountryCode", CountryCode.class.getCanonicalName());
            customTypeMapping.put("CurrencyCode", CurrencyCode.class.getCanonicalName());
        }

        final ClassLoader classLoader = getClass().getClassLoader();
        final Path inputPath = Paths.get("/Users/mkoester/Development/commercetools-importer/api-spec/api.raml");;
        //TODO set output path
        final Path outputPath = Paths.get(URI.create("file:/Users/mkoester/Development/rmf-codegen/gensrc"));
        generatorConfig = new GeneratorConfigBuilder()
                .packagePrefix("com.commercetools.importapi.models")
                .outputFolder(outputPath)
                .ramlFileLocation(inputPath)
                .javaDocProcessor(new DefaultJavaDocProcessor())
                .customTypeMapping(customTypeMapping)
                .build();
    }


    @Test
    public void testCodeGeneration() throws IOException {

        TestObserver<GenerationResult> resultTestObserver = new TestObserver<>();

        final GeneratorComponent generatorComponent = DaggerGeneratorComponent
                .builder()
                .generatorModule(GeneratorModule.of(generatorConfig, BeanGenerator::new, SpringClientCodeGenerator::new))
                .build();

        MasterCodeGenerator masterCodeGenerator = generatorComponent.getMasterCodeGenerator();

        masterCodeGenerator.generateStub().subscribe(resultTestObserver);
        resultTestObserver.assertComplete();
        resultTestObserver.assertNoErrors();
        resultTestObserver.assertValueCount(1);
        Assertions.assertThat(resultTestObserver.values().get(0).getGeneratedFiles()).isNotEmpty();
        logger.info("{} files were generated", resultTestObserver.values().get(0).getGeneratedFiles().size());

    }






}
