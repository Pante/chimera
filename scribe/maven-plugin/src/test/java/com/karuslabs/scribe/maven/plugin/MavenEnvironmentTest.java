/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.scribe.maven.plugin;

import com.karuslabs.scribe.core.Project;

import java.util.AbstractMap.SimpleEntry;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


class MavenEnvironmentTest {
    
    MavenEnvironment environment = new MavenEnvironment(Project.EMPTY);
    
    
    @Test
    void error() {
        environment.error("error");
        
        assertEquals(new SimpleEntry<>(null, "error"), environment.errors.get(0));
        assertTrue(environment.warnings.isEmpty());
    }
    
    
    @Test
    void error_location() {
        environment.error(Object.class, "error");
        
        assertEquals(new SimpleEntry<>(Object.class, "error"), environment.errors.get(0));
        assertTrue(environment.warnings.isEmpty());
    }
    
    
    @Test
    void warn() {
        environment.warn("warning");
        
        assertEquals(new SimpleEntry<>(null, "warning"), environment.warnings.get(0));
        assertTrue(environment.errors.isEmpty());
    }
    
    
    @Test
    void warn_location() {
        environment.warn(Object.class, "warning");
        
        assertEquals(new SimpleEntry<>(Object.class, "warning"), environment.warnings.get(0));
        assertTrue(environment.errors.isEmpty());
    }

} 
