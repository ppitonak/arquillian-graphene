package org.jboss.arquillian.graphene.page.interception;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.InstallableJavaScript;
import org.jboss.arquillian.graphene.javascript.JavaScript;

@JavaScript("Graphene.xhrInjection")
@Dependency(sources = "xhrInjection.js")
public interface XhrInjection extends InstallableJavaScript {

    void inject();

    void reset();
}
