package io.vrap.rmf.codegen.common;

import java.nio.file.Path;
import java.util.List;

public class GenerationResult {
    private final List<Path> generatedFiles;

    private GenerationResult(final List<Path> generatedFiles) {
        this.generatedFiles = generatedFiles;
    }

    public List<Path> getGeneratedFiles() {
        return generatedFiles;
    }

    public static GenerationResult of(final List<Path> generatedFiles) {
        return new GenerationResult(generatedFiles);
    }
}
