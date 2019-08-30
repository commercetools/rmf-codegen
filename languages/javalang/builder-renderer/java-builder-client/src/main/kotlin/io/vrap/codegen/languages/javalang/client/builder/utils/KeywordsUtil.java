package io.vrap.codegen.languages.javalang.client.builder.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KeywordsUtil {

    private final static Set<String> keywords;
    static {
        Set<String> s = new HashSet<String>();
        String [] kws = {
                "abstract", "continue",     "for",          "new",          "switch",
                "assert",   "default",      "if",           "package",      "synchronized",
                "boolean",  "do",           "goto",         "private",      "this",
                "break",    "double",       "implements",   "protected",    "throw",
                "byte",     "else",         "import",       "public",       "throws",
                "case",     "enum",         "instanceof",   "return",       "transient",
                "catch",    "extends",      "int",          "short",        "try",
                "char",     "final",        "interface",    "static",       "void",
                "class",    "finally",      "long",         "strictfp",     "volatile",
                "const",    "float",        "native",       "super",        "while",
                // literals
                "null",     "true",         "false"
        };
        for(String kw : kws)
            s.add(kw);
        keywords = Collections.unmodifiableSet(s);
    }
    
    public static boolean isKeyword(CharSequence s) {
        String keywordOrLiteral = s.toString();
        return keywords.contains(keywordOrLiteral);
    }
    
}
