package io.vrap.rmf.codegen.common.generator.doc;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Optional;

public class DefaultJavaDocProcessor extends JavaDocProcessor {

    private final static HtmlRenderer HTML_RENDERER = HtmlRenderer.builder().build();
    private final static Parser PARSER = Parser.builder().build();

    @Override
    protected Optional<String> markDownToJavaDoc(String markdownDescription) {

        return Optional.ofNullable(markdownDescription)
                .map(PARSER::parse)
                .map(HTML_RENDERER::render);
    }
}
