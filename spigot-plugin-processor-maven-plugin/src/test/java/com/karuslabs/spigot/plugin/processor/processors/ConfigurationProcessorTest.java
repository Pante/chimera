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

import com.karuslabs.spigot.plugin.processor.ProcessorException;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.karuslabs.spigot.plugin.processor.Yaml.TEST;


class ConfigurationProcessorTest {
    
    ConfigurationProcessor processor = spy(new ConfigurationProcessor() {
        @Override
        protected void executeConfiguration(ConfigurationSection config, String key) {
            
        }
    });
    
    
    @Test
    void orElse() {
        doNothing().when(processor).orElse(TEST, "non-configuration-section");
        
        processor.execute(TEST, "non-configuration-section");
        
        verify(processor).execute(TEST, "non-configuration-section");
    }

    
    @Test
    void orElse_ThrowsException() {
        assertEquals("Invalid type for: .non-configuration-section, value must be a ConfigurationSection",
            assertThrows(ProcessorException.class, () -> processor.orElse(TEST, "non-configuration-section")).getMessage()
        );
    }
    
}
