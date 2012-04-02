package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Method;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.JavascriptExecutor;

public class DefaultExecutionResolver implements ExecutionResolver {

    private static String CALL = "{0}.{1}({2})";

    private JavascriptExecutor browser = GrapheneContext.getProxyForInterfaces(JavascriptExecutor.class);

    @Override
    public Object execute(JSCall call) {
        checkBrowser();
        String script = resolveScriptToExecute(call);

        Object returnValue = browser.executeScript(script);
        return cast(call, returnValue);
    }

    private Object cast(JSCall call, Object returnValue) {
        Class<?> returnType = call.getMethod().getMethod().getReturnType();

        if (returnType.isEnum()) {
            return castToEnum(returnType, returnValue);
        }

        return returnValue;
    }

    private Object castToEnum(Class<?> returnType, Object returnValue) {
        try {
            Method method = returnType.getMethod("valueOf", String.class);
            return method.invoke(null, returnValue.toString());
        } catch (Exception e) {
            throw new IllegalStateException("returnValue '" + returnValue + "' can't be cast to enum value", e);
        }

    }

    private void checkBrowser() {
        if (!GrapheneContext.isInitialized()) {
            throw new IllegalStateException("current browser needs to be initialized; use GrapheneContext.set(browser)");
        }
        if (!GrapheneContext.holdsInstanceOf(JavascriptExecutor.class)) {
            throw new IllegalStateException("current browser needs to be instance of JavascriptExecutor");
        }
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

    protected String resolveArguments(JSCall call) {
        return StringUtils.join(call.getArguments(), ',');
    }

    protected String resolveOverloadedInterfaceName(JSTarget target) {
        return StringUtils.uncapitalize(target.getInterface().getSimpleName());
    }

}
