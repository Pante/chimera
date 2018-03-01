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
package com.karuslabs.spigot.plugin.processor.processors;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static com.karuslabs.spigot.plugin.processor.Yaml.TEST;


class DelegateProcessorTest {
    
    Processor delegate = mock(Processor.class);
    DelegateProcessor processor = new DelegateProcessor(delegate);
    
    
    @Test
    void executeConfiguration() {
        processor.executeConfiguration(TEST, "delegate");
        
        ConfigurationSection config = TEST.getConfigurationSection("delegate");
        
        verify(delegate).execute(config, "a");
        verify(delegate).execute(config, "b");
        verify(delegate).execute(config, "c");
    }
    
}
