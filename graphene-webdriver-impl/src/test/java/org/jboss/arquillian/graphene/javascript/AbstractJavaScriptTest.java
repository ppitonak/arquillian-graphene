package org.jboss.arquillian.graphene.javascript;

import java.text.MessageFormat;

public abstract class AbstractJavaScriptTest {
    
    public String invocation(String base, String method) {
        return MessageFormat.format("{0}.{1}.apply({0}, arguments)", base, method);
    }
}
