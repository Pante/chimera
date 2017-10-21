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
package com.karuslabs.commons.configuration;

import java.io.*;

import org.bukkit.configuration.file.FileConfiguration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BackedConfigurationTest {
    
    private BackedConfiguration config = new BackedConfiguration("configuration/config.yml");
    
    
    @Test
    void getString() {
        assertEquals("name", config.getString("location.world"));
    }
    
    
    @Test
    void save() throws IOException {
        config.config = mock(FileConfiguration.class);
        
        config.save();
        
        verify(config.config).save(config.file);
    }
    
    
    @Test
    void save_ThrowsException() throws IOException {
        config.config = mock(FileConfiguration.class);
        doThrow(IOException.class).when(config.config).save(config.file);
        
        assertThrows(UncheckedIOException.class, config::save);
    }
    
}
