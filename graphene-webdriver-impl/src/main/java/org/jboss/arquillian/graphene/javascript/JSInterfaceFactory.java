package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Proxy;

public class JSInterfaceFactory<T> {

    private Class<T> jsInterface;
    private JSInterfaceHandler handler;

    private JSInterfaceFactory(Class<T> jsInterface) {

        if (!jsInterface.isInterface()) {
            throw new IllegalArgumentException("interface must be provided");
        }

        this.jsInterface = jsInterface;
        ExecutionResolver resolver = getResolver();
        this.handler = new JSInterfaceHandler(jsInterface, resolver);
    }

    public static <T> JSInterfaceFactory<T> create(Class<T> jsInterface) {
        return new JSInterfaceFactory<T>(jsInterface);
    }

    @SuppressWarnings("unchecked")
    public T instantiate() {
        return (T) Proxy.newProxyInstance(jsInterface.getClassLoader(), new Class<?>[] { jsInterface }, handler);
    }

    private ExecutionResolver getResolver() {
        try {
            return getJavaScriptAnnotation().methodResolver().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("resolver can't be instantied");
        }
    }

    public JavaScript getJavaScriptAnnotation() {
        return jsInterface.getAnnotation(JavaScript.class);
    }
}
