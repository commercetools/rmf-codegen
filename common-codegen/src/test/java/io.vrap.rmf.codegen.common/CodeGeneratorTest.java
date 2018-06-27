package io.vrap.rmf.codegen.common;

import io.reactivex.observers.TestObserver;
import io.vrap.rmf.codegen.common.generator.MasterCodeGenerator;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeGeneratorTest {


    private GeneratorConfig generatorConfig;


    @Before
    public void init() {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("api-spec/api.raml").getFile());
        //TODO set output path
        final Path outputPath = Paths.get("/Users/abeniasaad/IdeaProjects/rmf-codegen/common-codegen/src/main/java");
        generatorConfig = new GeneratorConfigBuilder()
                .packagePrefix("com.commercetools")
                .outputFolder(outputPath)
                .ramlFileLocation(file.toPath())
                .build();
    }


    @Test
    public void testCodeGeneration() throws IOException {

        TestObserver<GenerationResult> resultTestObserver = new TestObserver<>();

        final GeneratorComponent generatorComponent = DaggerGeneratorComponent
                .builder()
                .generatorModule(GeneratorModule.of(generatorConfig, new DefaultJavaDocProcessor(),BeanGenerator::new))
                .build();

        MasterCodeGenerator masterCodeGenerator = generatorComponent.getMasterCodeGenerator();
        masterCodeGenerator.generateStub().subscribe(resultTestObserver);

        resultTestObserver.assertComplete();
        resultTestObserver.assertNoErrors();
        resultTestObserver.assertValueCount(1);
        Assertions.assertThat(resultTestObserver.values().get(0).getGeneratedFiles()).isNotEmpty();

    }


}
