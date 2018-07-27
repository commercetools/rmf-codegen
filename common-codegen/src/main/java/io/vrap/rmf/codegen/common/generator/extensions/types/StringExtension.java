package io.vrap.rmf.codegen.common.generator.extensions.types;

import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import org.apache.commons.lang3.StringUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

@ModelExtension(extend = String.class)
public class StringExtension {

    @ExtensionMethod
    public String getJavaIdentifier(final String input) {
        if (input.length() == 0) {
            return "_";
        }
        CharacterIterator ci = new StringCharacterIterator(input);
        StringBuilder sb = new StringBuilder();
        for (char c = ci.first(); c != CharacterIterator.DONE; c = ci.next()) {
            if (c == ' ')
                c = '_';
            if (sb.length() == 0) {
                if (Character.isJavaIdentifierStart(c)) {
                    sb.append(c);
                    continue;
                } else
                    sb.append('_');
            }
            if (Character.isJavaIdentifierPart(c)) {
                sb.append(c);
            } else {
                sb.append('_');
            }
        }
        ;
        return sb.toString();

    }


    @ExtensionMethod
    public String getEnumValueName(final String input) {
        return getJavaIdentifier(input).toUpperCase();
    }

    @ExtensionMethod
    public String getCapitalize(final String input) {
        return StringUtils.capitalize(input);
    }



}
