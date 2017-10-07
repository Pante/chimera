/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.locale;

import com.karuslabs.commons.locale.resources.EmbeddedResource;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;


@TestInstance(PER_CLASS)
public class ExternalControlTest {
    
    private ExternalControl control = new ExternalControl(new EmbeddedResource("locale/resources/properties"), new EmbeddedResource("locale/resources/yaml"));
    
    
    @ParameterizedTest
    @MethodSource("newBundle_parameters")
    public void newBundle(Locale locale, String expected) {
        assertEquals(expected, ResourceBundle.getBundle("Resource", locale, control).getString("test"));
    }
    
    static Stream<Arguments> newBundle_parameters() {
        return Stream.of(of(new Locale("en", "GB"), "English"), of(new Locale("zh", "CN"), "Chinese"));
    }    
    
    
    @Test
    public void load() {
        assertNull(control.load("", null));
    }
    
    
    @Test
    public void getFormats() {
        assertEquals(asList("properties", "yml", "yaml"), control.getFormats(null));
    }
    
}
