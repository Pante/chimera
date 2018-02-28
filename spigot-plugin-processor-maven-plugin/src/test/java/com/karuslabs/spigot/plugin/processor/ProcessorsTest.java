/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.spigot.plugin.processor;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import org.junit.jupiter.api.Test;

import static com.karuslabs.spigot.plugin.processor.Yaml.PLUGIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProcessorsTest {
    
    Log log;
    Processors processors;
    
    
    ProcessorsTest() {
        log = mock(Log.class);
        processors = Processors.simple(log, null, null, null, true);
        processors.required.clear();
    }
    
    
    @Test
    void execute() throws MojoExecutionException {
        processors.execute(PLUGIN.getConfigurationSection("plugin"));
    }
    
    
    @Test
    void execute_Value_ThrowsException() {
        assertEquals(
            ProcessorException.class,
            assertThrows(MojoExecutionException.class, () -> processors.execute(PLUGIN.getConfigurationSection("invalid-value"))).getCause().getClass()
        );
    }
    
    @Test
    void execute_Key_ThrowsException() {
        assertEquals(
            "Invalid keys: [loadafter] in plugin.yml, key must be valid: " + processors.processors.keySet(),
            assertThrows(MojoExecutionException.class, () -> processors.execute(PLUGIN.getConfigurationSection("invalid-keys"))).getMessage()
        );
    }
    
}
