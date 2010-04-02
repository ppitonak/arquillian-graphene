/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.iteration.ChildElementList;
import org.jboss.test.selenium.locator.iteration.ElementOcurrenceList;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

public class JQueryLocator extends AbstractElementLocator implements IterableLocator<JQueryLocator>,
    CompoundableLocator<JQueryLocator> {

    public JQueryLocator(String jquerySelector) {
        super(jquerySelector);
    }

    public LocationStrategy getLocationStrategy() {
        return LocationStrategy.JQUERY;
    }

    public JQueryLocator getNthChildElement(int index) {
        return new JQueryLocator(format("{0}:nth-child({1})", getLocator(), index + 1));
    }

    public JQueryLocator getNthOccurence(int index) {
        return new JQueryLocator(format("{0}:eq({1})", getLocator(), index));
    }

    public Iterable<JQueryLocator> getAllChildren() {
        return new ChildElementList<JQueryLocator>(this.getChild(LocatorFactory.jq("*")));
    }

    public Iterable<JQueryLocator> getChildren(JQueryLocator elementLocator) {
        return new ChildElementList<JQueryLocator>(this.getChild(elementLocator));
    }

    public Iterable<JQueryLocator> getDescendants(JQueryLocator elementLocator) {
        return new ElementOcurrenceList<JQueryLocator>(this.getDescendant(elementLocator));
    }

    public JQueryLocator getChild(JQueryLocator elementLocator) {
        return new JQueryLocator(format("{0} > {1}", getLocator(), elementLocator.getLocator()));
    }

    public JQueryLocator getDescendant(JQueryLocator elementLocator) {
        return new JQueryLocator(format("{0} {1}", getLocator(), elementLocator.getLocator()));
    }

}