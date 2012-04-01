package org.jboss.arquillian.graphene.javascript;

class JSTarget {

    private Class<?> jsInterface;
    private JavaScript annotation;
    private ExecutionResolver resolver;

    public JSTarget(Class<?> jsInterface) {
        this.jsInterface = jsInterface;
        this.annotation = resolveAnnotation();
        this.resolver = createResolver(annotation);

    }

    public Class<?> getInterface() {
        return jsInterface;
    }

    public String getName() {
        if ("".equals(annotation.value())) {
            return null;
        }
        return annotation.value();
    }

    public JavaScript resolveAnnotation() {
        return jsInterface.getAnnotation(JavaScript.class);
    }

    public ExecutionResolver getResolver() {
        return resolver;
    }

    private ExecutionResolver createResolver(JavaScript annotation) {
        try {
            return annotation.methodResolver().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("resolver " + annotation.methodResolver() + " can't be instantied", e);
        }
    }
}
