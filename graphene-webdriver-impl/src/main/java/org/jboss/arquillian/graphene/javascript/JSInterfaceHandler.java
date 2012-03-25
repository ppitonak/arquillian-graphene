package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JSInterfaceHandler implements InvocationHandler {

    private Class<?> jsInterface;
    private ExecutionResolver resolver;

    public JSInterfaceHandler(Class<?> jsInterface, ExecutionResolver resolver) {
        this.jsInterface = jsInterface;
        this.resolver = resolver;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

}
