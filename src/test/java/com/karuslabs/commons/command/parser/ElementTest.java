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
package com.karuslabs.commons.command.parser;

import java.util.*;

import junitparams.*;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ElementTest {
    
    private Element<String> element;
    private Map<String, String> definitions;
    
    
    public ElementTest() {
        element = spy(new Element<String>(definitions = spy(new HashMap<>())) {});
    }
    
    
    @Before
    public void setup() {
        definitions.clear();
    }
    
    
    @Test
    @Parameters({"value, false", ", true"})
    public void define(String value, boolean expected) {
        doReturn(value.isEmpty() ? null : value).when(element).parse("");
        
        element.define("key", "");
        
        assertEquals(expected, element.getDefinitions().isEmpty());
    }
    
    
    @Test
    @Parameters
    public void parse(Object value, String expected, int times) {
        element.getDefinitions().put("value", "");
        
        assertEquals(expected, element.parse(value));
        verify(element, times(times)).parse(any(ConfigurationSection.class));
    }
    
    protected Object[] parametersForParse() {
        ConfigurationSection config = mock(ConfigurationSection.class);
        return new Object[] {
            new Object[] {"value", "", 0},
            new Object[] {config, null, 1},
            new Object[] {1, null, 0}
        };
    }
    
}
