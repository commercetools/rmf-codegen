package io.vrap.rmf.codegen.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import com.sun.istack.internal.localization.LocalizableMessage;
import io.reactivex.observers.TestObserver;
import io.vrap.rmf.codegen.common.generator.MasterCodeGenerator;
import io.vrap.rmf.codegen.common.generator.client.ClientCodeGenerator;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Arrays;
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
        final Path inputPath = null;
        //TODO set output path
        final Path outputPath = null;
        generatorConfig = new GeneratorConfigBuilder()
                .packagePrefix("com.commercetools")
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
                .generatorModule(GeneratorModule.of(generatorConfig, BeanGenerator::new, ClientCodeGenerator::new))
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
