package io.vrap.rmf.codegen.common.generator.core;


import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.List;


@Value.Immutable
@Value.Style(
        visibility = Value.Style.ImplementationVisibility.PRIVATE,
        builderVisibility = Value.Style.BuilderVisibility.PUBLIC
)
public interface GenerationResult {


    static GenerationResult of(final List<Path> generatedFiles) {
        return new GenerationResultBuilder().addAllGeneratedFiles(generatedFiles).build();
    }

    List<Path> getGeneratedFiles();


}
