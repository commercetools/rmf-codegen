package io.vrap.rmf.codegen.common.generator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CtAnnotationProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(CtAnnotationProcessor.class);

    public static void processAnnotations(final File outputDir, final List<Class> annotations, final List<JavaFileObject> compilationUnits) {

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {

            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(outputDir));
            fileManager.setLocation(StandardLocation.SOURCE_OUTPUT, Arrays.asList(outputDir));
            fileManager.setLocation(StandardLocation.CLASS_PATH, getClassPathPath(annotations));

            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics,
                    Arrays.asList("-proc:only"),
                    null, compilationUnits);
            boolean success = task.call();
            //TODO handle exception with log
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                //TODO handle all diagnostic kinds
                LOGGER.warn("Diagnostic kind " + diagnostic.getKind() + ", message " + diagnostic.getMessage(Locale.ENGLISH));
            }
        }
        //TODO handle exception with log
        catch (IOException e) {
            LOGGER.error("Annotation processing error", e);
        }
    }


    private static Set<File> getClassPathPath(final List<Class> annotations) {
        Objects.nonNull(annotations);
        final Set<File> resultLibs = annotations.stream()
                .map(annotation -> annotation.getProtectionDomain().getCodeSource().getLocation().getPath())
                .map(File::new)
                .collect(Collectors.toSet());

        return resultLibs;
    }
}
