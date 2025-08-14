package io.github.yangdaowan.surfear.core.interceptor.support.template.engine.processor;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SimpleDialect extends AbstractProcessorDialect {

    public SimpleDialect() {
        super("Simple Dialect", "simple", 1000);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new PatternTextProcessor( Pattern.compile("\\$\\{(.+?)\\}"), "${", "}", 1002));
//        processors.add(new PatternTextProcessor( Pattern.compile("\\{\\{(.*?)\\}\\}"), "{{", "}}",  1001));
        return processors;
    }
}

