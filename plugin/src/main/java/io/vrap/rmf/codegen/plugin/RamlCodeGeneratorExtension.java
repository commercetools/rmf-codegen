package io.vrap.rmf.codegen.plugin;

import java.io.File;
import java.util.Map;

public class RamlCodeGeneratorExtension {


    private File ramlFileLocation;
    private File outputFolder;
    private String packagePrefix;
    private Map<String,String> customTypeMapping;

    public RamlCodeGeneratorExtension(File outputFolder, String packagePrefix) {
        this.outputFolder = outputFolder;
        this.packagePrefix = packagePrefix;
    }

    public File getRamlFileLocation() {
        return ramlFileLocation;
    }

    public void setRamlFileLocation(File ramlFileLocation) {
        this.ramlFileLocation = ramlFileLocation;
    }

    public File getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(File outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String getPackagePrefix() {
        return packagePrefix;
    }

    public void setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    public Map<String, String> getCustomTypeMapping() {
        return customTypeMapping;
    }

    public void setCustomTypeMapping(Map<String, String> customTypeMapping) {
        this.customTypeMapping = customTypeMapping;
    }
}
