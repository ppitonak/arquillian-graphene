package org.jboss.arquillian.graphene.javascript;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.JavascriptExecutor;

public class DefaultExecutionResolver implements ExecutionResolver {

    private static String CALL = "{0}.{1}({2})";

    private JavascriptExecutor browser = GrapheneContext.getProxyForInterfaces(JavascriptExecutor.class);

    @Override
    public void execute(JSCall call) {
        String script = resolveScriptToExecute(call);
        browser.executeScript(script);
    }

    protected String resolveScriptToExecute(JSCall call) {
        return MessageFormat.format(CALL, resolveTargetName(call.getTarget()), resolveMethodName(call), resolveArguments(call));
    }

    protected String resolveTargetName(JSTarget target) {
        if (target.getName() != null) {
            return target.getName();
        }
        return resolveOverloadedInterfaceName(target);
    }

    protected String resolveMethodName(JSCall call) {
        return call.getMethod().getName();
    }

    protected Object[] resolveArguments(JSCall call) {
        return call.getArguments();
    }

    protected String resolveOverloadedInterfaceName(JSTarget target) {
        String name = target.getInterface().getSimpleName();

        List<String> options = new LinkedList<String>();
        options.add(StringUtils.capitalize(name));
        options.add(StringUtils.uncapitalize(name));

        String separatedOptions = StringUtils.join(options, " || ");

        return "(" + separatedOptions + ")";
    }

}
