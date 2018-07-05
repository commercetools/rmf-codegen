package io.vrap.rmf.codegen.plugin;


import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.nio.file.Paths;

public class RamlCodegenPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        final File defaultOutputFolder = Paths.get(project.getBuildDir().getPath(), "/generated-sources/main/java").toFile();
        final String defaultPrefix = "io.vrap.rmf.codegen";

        final RamlCodeGeneratorExtension extension = project.getExtensions().create("generateEntities", RamlCodeGeneratorExtension.class, defaultOutputFolder, defaultPrefix);

        project.getTasks().create("generateEntities", RamlCodeGeneratorTask.class)
                .setPackagePrefix(extension.getPackagePrefix())
                .setOutputFolder(extension.getOutputFolder())
                .setCustomTypeMapping(extension.getCustomTypeMapping());
    }


}