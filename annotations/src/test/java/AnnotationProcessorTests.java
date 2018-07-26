import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.codegen.common.processor.processor.ModelExtensionProcessor;
import org.junit.Test;

import javax.tools.JavaFileObject;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class AnnotationProcessorTests {


    @Test
    public void errorForAnnotatedClass() throws Exception{
        URL url = Paths.get("/Users/abeniasaad/IdeaProjects/rmf-codegen/annotations/src/main/java/io/vrap/rmf/codegen/common/processor/extension/example/StringExtension.java").toUri().toURL();
        JavaFileObject INPUT_FILE = JavaFileObjects.forResource(url);
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(Arrays.asList(INPUT_FILE))
                .processedWith(new ModelExtensionProcessor())
                .compilesWithoutError();
    }
}
