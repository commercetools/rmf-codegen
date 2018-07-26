package io.vrap.rmf.codegen.common;

import com.google.inject.Injector;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import io.reactivex.Flowable;
import io.reactivex.observers.TestObserver;
import io.vrap.rmf.codegen.common.generator.MasterCodeGenerator;
import io.vrap.rmf.codegen.common.generator.client.spring.SpringClientCodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.CodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.GenerationResult;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfigBuilder;
import io.vrap.rmf.codegen.common.generator.extensions.JavaSTFileSwitch;
import io.vrap.rmf.codegen.common.generator.extensions.RmfModelAdaptor;
import io.vrap.rmf.codegen.common.generator.extensions.STCodeGenerator;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorComponent;
import io.vrap.rmf.codegen.common.generator.injection.GeneratorModule;
import io.vrap.rmf.codegen.common.generator.model.codegen.BeanGenerator;
import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapperFactory;
import io.vrap.rmf.raml.model.types.StringType;
import org.assertj.core.api.Assertions;
import org.eclipse.emf.common.util.URI;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

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

        final URL url = CodeGenerator.class.getResource("/api-spec/api.raml");
        final URI ramlFileLocation = URI.createURI(url.toString());
        final String gensrc = System.getProperty("GENSRC");
        final String current = System.getProperty("user.dir");
        final Path outputPath =  Paths.get( "/Users/abeniasaad/IdeaProjects/rmf-codegen/common-codegen/src/main/java") ;

        generatorConfig = new GeneratorConfigBuilder()
                .packagePrefix("com.commercetools.importapi.models")
                .outputFolder(outputPath)
                .ramlFileLocation(ramlFileLocation)
                .customTypeMapping(customTypeMapping)
                .build();

        generatorComponent = new GeneratorComponent(GeneratorModule.of(generatorConfig, BeanGenerator::new, SpringClientCodeGenerator::new));
    }


    @Test
    public void testCodeGeneration() throws IOException {

        TestObserver<GenerationResult> resultTestObserver = new TestObserver<>();
        MasterCodeGenerator masterCodeGenerator = generatorComponent.getMasterCodeGenerator();

        masterCodeGenerator.generateStub().subscribe(resultTestObserver);
        resultTestObserver.assertComplete();
        resultTestObserver.assertNoErrors();
        resultTestObserver.assertValueCount(1);
        Assertions.assertThat(resultTestObserver.values().get(0).getGeneratedFiles()).isNotEmpty();
        logger.info("{} files were generated", resultTestObserver.values().get(0).getGeneratedFiles().size());

    }


    @Test
    public void testTemplate() throws Exception {
        STCodeGenerator stCodeGenerator = generatorComponent.getStCodeGenerator();
        stCodeGenerator.generateClasses().subscribe(generationResult -> System.out.println(generationResult));
    }


}
