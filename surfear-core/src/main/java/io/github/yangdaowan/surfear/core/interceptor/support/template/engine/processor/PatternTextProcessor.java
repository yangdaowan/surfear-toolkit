package io.github.yangdaowan.surfear.core.interceptor.support.template.engine.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IText;
import org.thymeleaf.processor.text.AbstractTextProcessor;
import org.thymeleaf.processor.text.ITextStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTextProcessor extends AbstractTextProcessor{

    // 正则表达式匹配 {{...}} ${...}
    private final Pattern pattern;
    private final String prefix;  // 如 "${" 或 "{{"
    private final String suffix;

    public PatternTextProcessor(Pattern pattern, String prefix, String suffix, int precedence) {
        super(TemplateMode.TEXT, precedence);
        this.pattern = pattern;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public void doProcess(
            ITemplateContext context,
            IText text,
            ITextStructureHandler structureHandler) {

        final String original = text.getText();
        final Matcher matcher = pattern.matcher(original);
        final StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            final String expression = matcher.group(1);
            final Object value = evaluateExpression(context, expression);
            matcher.appendReplacement(result, value != null ? value.toString() : "");
        }
        matcher.appendTail(result);

        structureHandler.setText(result.toString());
    }

    private Object evaluateExpression(ITemplateContext context, String expression) {
        Object value = context.getVariable(expression);
        if (value != null) {
            return value;
        }

        final IStandardExpression standardExpression =
                StandardExpressions.getExpressionParser(context.getConfiguration())
                        .parseExpression(context, this.prefix + expression + this.suffix);
        return standardExpression.execute(context);
    }
}