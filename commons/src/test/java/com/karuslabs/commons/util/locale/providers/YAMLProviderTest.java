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
package com.karuslabs.commons.util.locale.providers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import org.bukkit.configuration.file.YamlConfiguration;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class YAMLProviderTest {
    
    static final File source;
    static final UUID id = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5");
            
    YAMLProvider provider = new YAMLProvider(source);
    
    static {
        try {
            source = new File(YAMLProviderTest.class.getClassLoader().getResource("util/locale/provider/locales.yml").toURI());
            
        } catch (URISyntaxException ignored) {
            throw new RuntimeException();
        }
    }

    
    @Test
    void get() {
        assertEquals(Locale.KOREA, provider.get(id));
    }
    
    
    @Test
    void get_default() {
        assertEquals(Locale.getDefault(), provider.get(UUID.randomUUID()));
    }
    
    
    @Test
    void getDefault() {
        assertEquals(Locale.getDefault(), provider.getDefault());
    }
    
    
    @Test
    void configuration() {
        assertEquals(provider.configuration, provider.configuration());
    }
    
    
    @Test
    void save() throws IOException {
        provider.configuration = mock(YamlConfiguration.class);
        
        provider.save();
        
        verify(provider.configuration).save(source);
    }
    
}
