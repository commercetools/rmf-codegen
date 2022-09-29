package io.vrap.rmf.codegen.cli.diff;

enum CheckOptionType {
    EXCLUDE("exclude");

    private final String type;

    CheckOptionType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }


}
