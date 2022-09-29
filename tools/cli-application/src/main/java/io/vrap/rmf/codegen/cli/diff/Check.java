package io.vrap.rmf.codegen.cli.diff;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Check {
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlElementWrapper(localName = "options")
    private List<CheckOption> options;

    @JacksonXmlProperty(localName = "severity")
    private CheckSeverity severity;

    @JsonCreator
    public Check() {
    }

    public Check(String name, CheckSeverity severity, List<CheckOption> options) {
        this.name = name;
        this.options = options;
        this.severity = severity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CheckOption> getOptions() {
        return options;
    }

    public void setOptions(List<CheckOption> options) {
        this.options = options;
    }

    public CheckSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(CheckSeverity severity) {
        this.severity = severity;
    }
}
