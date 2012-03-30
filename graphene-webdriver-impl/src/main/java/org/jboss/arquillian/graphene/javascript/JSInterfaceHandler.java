package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JSInterfaceHandler implements InvocationHandler {

    private JSTarget target;

    public JSInterfaceHandler(JSTarget jsTarget) {
        this.target = jsTarget;
    }
    
    public JSTarget getTarget() {
        return target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        JSCall call = new JSCall(new JSMethod(target, method), args);
        target.getResolver().execute(call);
        return null;
    }

}
