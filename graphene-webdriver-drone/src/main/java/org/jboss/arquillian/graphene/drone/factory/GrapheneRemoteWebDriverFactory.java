/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.graphene.drone.factory;

import java.net.URL;

import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.drone.configuration.RemoteWebDriverConfiguration;
import org.jboss.arquillian.graphene.remote.reusable.PersistReusedSessionsEvent;
import org.jboss.arquillian.graphene.remote.reusable.ReusableRemoteWebDriver;
import org.jboss.arquillian.graphene.remote.reusable.ReusedSession;
import org.jboss.arquillian.graphene.remote.reusable.InitializationParameter;
import org.jboss.arquillian.graphene.remote.reusable.InitializationParametersMap;
import org.jboss.arquillian.graphene.remote.reusable.ReusedSessionStore;
import org.jboss.arquillian.graphene.remote.reusable.UnableReuseSessionException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Extends the {@link RemoteWebDriverFactory} and provides the created instance to the {@link GrapheneContext}.
 * 
 * Reuses the browser session on remote selenium server if it's configured.
 *  
 * @author Lukas Fryc
 * 
 */
public class GrapheneRemoteWebDriverFactory extends RemoteWebDriverFactory implements
        Configurator<RemoteWebDriver, TypedWebDriverConfiguration<RemoteWebDriverConfiguration>>,
        Instantiator<RemoteWebDriver, TypedWebDriverConfiguration<RemoteWebDriverConfiguration>>, Destructor<RemoteWebDriver> {

    private static final boolean REUSABLE = true;

    @Inject
    Instance<ReusedSessionStore> sessionStore;
    
    @Inject
    Instance<InitializationParametersMap> initParams;

    @Inject
    Event<PersistReusedSessionsEvent> persistEvent;

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.drone.spi.Sortable#getPrecedence()
     */
    @Override
    public int getPrecedence() {
        return 20;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.drone.spi.Destructor#destroyInstance(java.lang.Object)
     */
    @Override
    public void destroyInstance(RemoteWebDriver driverProxy) {
        try {
            if (REUSABLE) {
                RemoteWebDriver driver = ((GrapheneProxyInstance) driverProxy).unwrap();
                InitializationParameter params = initParams.get().get(driver);
                
                // we can reuse browser only when it has not been quited
                if (driver.getSessionId() != null) {
                    ReusedSession session = new ReusedSession(driver.getSessionId(), driver.getCapabilities());
                    sessionStore.get().store(params, session);
                    initParams.get().remove(driver);
                    persistEvent.fire(new PersistReusedSessionsEvent());
                }
            } else {
                super.destroyInstance(driverProxy);
            }
        } finally {
            GrapheneContext.reset();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.drone.spi.Instantiator#createInstance(org.jboss.arquillian.drone.spi.DroneConfiguration)
     */
    @Override
    public RemoteWebDriver createInstance(TypedWebDriverConfiguration<RemoteWebDriverConfiguration> configuration) {
        RemoteWebDriver driver = null;
        
        if (REUSABLE) {
            InitializationParameter key = getReusedSessionKey(configuration);
            ReusedSession stored = sessionStore.get().pull(key);
            
            if (stored != null) {
                try {
                    driver = new ReusableRemoteWebDriver(key.getUrl(), stored.getCapabilities(), stored.getSessionId());
                } catch (UnableReuseSessionException e) {
                    // TODO logger
                    System.err.println("Unable to reuse session: " + stored.getSessionId());
                }
            }
            
            if (driver == null) {
                driver = super.createInstance(configuration);
            }
            
            initParams.get().put(driver, key);
        } else {
            driver = super.createInstance(configuration);
        }

        RemoteWebDriver proxy = GrapheneContext.getProxyForDriver(RemoteWebDriver.class);
        GrapheneContext.set(driver);
        return proxy;
    }
    
    protected InitializationParameter getReusedSessionKey(TypedWebDriverConfiguration<RemoteWebDriverConfiguration> configuration) {
        URL hubUrl = getHubUrl(configuration);
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities(configuration);
        return new InitializationParameter(hubUrl, desiredCapabilities);
    }
}