/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opencloudengine.flamingo2.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Logback {@link ch.qos.logback.core.pattern.CompositeConverter} colors output using the {@link org.opencloudengine.flamingo2.logging.AnsiOutput} class. A
 * single 'color' option can be provided to the converter, or if not specified color will
 * be picked based on the logging level.
 *
 * @author Phillip Webb
 */
public class ColorConverter extends CompositeConverter<ILoggingEvent> {

    private static final Map<String, org.opencloudengine.flamingo2.logging.AnsiElement> ELEMENTS;

    static {
        Map<String, org.opencloudengine.flamingo2.logging.AnsiElement> elements = new HashMap<String, org.opencloudengine.flamingo2.logging.AnsiElement>();
        elements.put("faint", org.opencloudengine.flamingo2.logging.AnsiElement.FAINT);
        elements.put("red", org.opencloudengine.flamingo2.logging.AnsiElement.RED);
        elements.put("green", org.opencloudengine.flamingo2.logging.AnsiElement.GREEN);
        elements.put("yellow", org.opencloudengine.flamingo2.logging.AnsiElement.YELLOW);
        elements.put("blue", org.opencloudengine.flamingo2.logging.AnsiElement.BLUE);
        elements.put("magenta", org.opencloudengine.flamingo2.logging.AnsiElement.MAGENTA);
        elements.put("cyan", org.opencloudengine.flamingo2.logging.AnsiElement.CYAN);
        ELEMENTS = Collections.unmodifiableMap(elements);
    }

    private static final Map<Integer, org.opencloudengine.flamingo2.logging.AnsiElement> LEVELS;

    static {
        Map<Integer, org.opencloudengine.flamingo2.logging.AnsiElement> levels = new HashMap<Integer, org.opencloudengine.flamingo2.logging.AnsiElement>();
        levels.put(Level.ERROR_INTEGER, org.opencloudengine.flamingo2.logging.AnsiElement.RED);
        levels.put(Level.WARN_INTEGER, org.opencloudengine.flamingo2.logging.AnsiElement.YELLOW);
        LEVELS = Collections.unmodifiableMap(levels);
    }

    @Override
    protected String transform(ILoggingEvent event, String in) {
        org.opencloudengine.flamingo2.logging.AnsiElement element = ELEMENTS.get(getFirstOption());
        if (element == null) {
            // Assume highlighting
            element = LEVELS.get(event.getLevel().toInteger());
            element = (element == null ? org.opencloudengine.flamingo2.logging.AnsiElement.GREEN : element);
        }
        return toAnsiString(in, element);
    }

    protected String toAnsiString(String in, org.opencloudengine.flamingo2.logging.AnsiElement element) {
        return org.opencloudengine.flamingo2.logging.AnsiOutput.toString(element, in);
    }

}
