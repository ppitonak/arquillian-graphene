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

import java.lang.annotation.Annotation;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.factory.FirefoxDriverFactory;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.drone.configuration.RemoteWebDriverConfiguration;
import org.jboss.arquillian.graphene.utils.URLUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Extends the {@link FirefoxDriverFactory} and provides the created instance to the {@link GrapheneContext}.
 * 
 * @author Lukas Fryc
 * 
 */
public class RemoteWebDriverFactory implements
        Configurator<RemoteWebDriver, TypedWebDriverConfiguration<RemoteWebDriverConfiguration>>,
        Instantiator<RemoteWebDriver, TypedWebDriverConfiguration<RemoteWebDriverConfiguration>>, Destructor<RemoteWebDriver> {

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.drone.spi.Sortable#getPrecedence()
     */
    public int getPrecedence() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.drone.spi.Destructor#destroyInstance(java.lang.Object)
     */
    public void destroyInstance(RemoteWebDriver instance) {
        instance.quit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.drone.spi.Instantiator#createInstance(org.jboss.arquillian.drone.spi.DroneConfiguration)
     */
    public RemoteWebDriver createInstance(TypedWebDriverConfiguration<RemoteWebDriverConfiguration> configuration) {
        return new RemoteWebDriver(URLUtils.buildUrl("http://127.0.0.1:4444/wd/hub"), DesiredCapabilities.firefox());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.drone.spi.Configurator#createConfiguration(org.jboss.arquillian.config.descriptor.api.
     * ArquillianDescriptor, java.lang.Class)
     */
    public TypedWebDriverConfiguration<RemoteWebDriverConfiguration> createConfiguration(ArquillianDescriptor descriptor,
            Class<? extends Annotation> qualifier) {
        return new TypedWebDriverConfiguration<RemoteWebDriverConfiguration>(RemoteWebDriverConfiguration.class,
                "org.openqa.selenium.remote.RemoteWebDriver").configure(descriptor, qualifier);
    }
}