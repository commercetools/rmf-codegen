package io.vrap.rmf.codegen.cli.diff;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.vrap.rmf.codegen.cli.diff.Check;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "diff")
public class DiffConfiguration {
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "apply")
    private List<Apply> apply = new ArrayList<>();

    @JacksonXmlElementWrapper(localName = "checks")
    @JacksonXmlProperty(localName = "check")
    private List<Check> checks = new ArrayList<>();

    @JsonCreator
    public DiffConfiguration() {
    }

    public DiffConfiguration(String name, List<Check> checks) {
        this.name = name;
        this.checks = checks;
    }

    public DiffConfiguration(String name, List<Check> checks, List<Apply> apply) {
        this.name = name;
        this.checks = checks;
        this.apply = apply;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public void setChecks(List<Check> checks) {
        this.checks = checks;
    }

    public List<Apply> getApply() {
        return apply;
    }

    public void setApply(List<Apply> apply) {
        this.apply = apply;
    }
}

