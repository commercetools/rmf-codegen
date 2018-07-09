package io.vrap.rmf.codegen.plugin;


import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.nio.file.Paths;

public class RamlCodegenPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        RamlCodeGeneratorTask task = project.getTasks().create("generateRamlStub", RamlCodeGeneratorTask.class);
    }


}