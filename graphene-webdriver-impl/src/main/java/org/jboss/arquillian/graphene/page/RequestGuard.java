package org.jboss.arquillian.graphene.page;

import org.jboss.arquillian.graphene.javascript.JavaScript;

@JavaScript("Graphene.xhrGuard")
public interface RequestGuard {

    RequestType getRequestDone();

    RequestType clearRequestDone();

    boolean isRequestDone();
}