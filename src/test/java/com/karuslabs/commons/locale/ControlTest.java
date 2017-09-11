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

import junitparams.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;


@RunWith(JUnitParamsRunner.class)
public class ControlTest {
    
    private Control control;
    
    
    public ControlTest() {
        control = new Control(new EmbeddedResource("locale/resources/properties"), new EmbeddedResource("locale/resources/yaml"));
    }
    
    
    @Test
    @Parameters
    public void newBundle(Locale locale, String expected) {
        ResourceBundle bundle = ResourceBundle.getBundle("Resource", locale, control);
        assertEquals(expected, bundle.getString("test"));
    }
    
    protected Object[] parametersForNewBundle() {
        return new Object[] {
            new Object[] {new Locale("en", "US"), "English"},
            new Object[] {new Locale("zh", "CN"), "Chinese"},
        };
    }
    
    
    @Test
    public void getFormats() {
        assertThat(control.getFormats(null), equalTo(asList("properties", "yml", "yaml")));
    }
    
}
