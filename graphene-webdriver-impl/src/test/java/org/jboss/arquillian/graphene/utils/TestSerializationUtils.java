/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Serializable;

import org.junit.Test;

/**
 * @author Lukas Fryc
 */
public class TestSerializationUtils {

    @Test
    public void testObjectSerializationToBytes() throws IOException, ClassNotFoundException {
        // given
        String payload = CustomObject.class.getName();
        Bean bean = new Bean();
        CustomObject object = new CustomObject(payload, bean);

        // then
        byte[] serialized = SerializationUtils.serializeToBytes(object);
        CustomObject deserialized = SerializationUtils.deserializeFromBytes(serialized);

        // then
        assertEquals(payload, deserialized.payload);
        assertNotNull(deserialized.bean);
    }

    @SuppressWarnings("serial")
    private static class CustomObject implements Serializable {

        private Bean bean;

        private String payload;

        public CustomObject(String payload, Bean bean) {
            this.payload = payload;
            this.bean = bean;
        }
    }

    @SuppressWarnings("serial")
    private static class Bean implements Serializable {

    }
}
