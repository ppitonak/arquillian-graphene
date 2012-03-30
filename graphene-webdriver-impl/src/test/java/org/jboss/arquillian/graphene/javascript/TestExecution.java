package org.jboss.arquillian.graphene.javascript;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.TestingDriverStub;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;

public class TestExecution {

    @JavaScript
    public static interface Interface {
        public void method();
    }

    @Test
    public void test_execution() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());

        // when
        GrapheneContext.set(executor);
        JSInterfaceFactory<Interface> factory = JSInterfaceFactory.create(Interface.class);
        Interface instance = factory.instantiate();
        instance.method();

        // then
        verify(executor, only()).executeScript("(Interface || interface).method()");
    }
}
