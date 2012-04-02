package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.JavascriptExecutor;

public class DefaultExecutionResolver implements ExecutionResolver {

    private static String CALL = "{0}.{1}.apply({0}, arguments)";

    private JavascriptExecutor browser = GrapheneContext.getProxyForInterfaces(JavascriptExecutor.class);

    @Override
    public Object execute(JSCall call) {
        checkBrowser();
        String script = resolveScriptToExecute(call);

        Object[] arguments = castArguments(call.getArguments());
        Object returnValue = browser.executeScript(script, arguments);
        return castResult(call, returnValue);
    }

    private Object[] castArguments(Object[] arguments) {
        Object[] result = Arrays.copyOf(arguments, arguments.length);
        
        for (int i = 0; i < result.length; i++) {
            Object arg = result[i];
            
            if (arg.getClass().isEnum()) {
                result[i] = castEnumToString(arg);
            }
        }
        
        return result;
    }
    
    private Object castEnumToString(Object enumValue) {
        return enumValue.toString();
    }

    private Object castResult(JSCall call, Object returnValue) {
        Class<?> returnType = call.getMethod().getMethod().getReturnType();

        if (returnType.isEnum()) {
            return castStringToEnum(returnType, returnValue);
        }

        return returnValue;
    }

    private Object castStringToEnum(Class<?> returnType, Object returnValue) {
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
        return MessageFormat.format(CALL, resolveTargetName(call.getTarget()), resolveMethodName(call));
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

    protected String resolveOverloadedInterfaceName(JSTarget target) {
        return StringUtils.uncapitalize(target.getInterface().getSimpleName());
    }

}
