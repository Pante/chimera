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
package com.karuslabs.commons.locale.providers;

import com.karuslabs.commons.configuration.BackedConfiguration;

import java.util.*;

import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ConfigurationProviderTest {
    
    private BackedConfiguration config = mock(BackedConfiguration.class);
    private ConfigurationProvider provider = new ConfigurationProvider(config);
    private Player player = when(mock(Player.class).getUniqueId()).thenReturn(UUID.randomUUID()).getMock();
    
    
    @Test
    public void get() {
        when(config.getString(player.getUniqueId().toString())).thenReturn("ja_JP");
        
        assertEquals(Locale.JAPAN, provider.get(player));
    }
    
    
    @ParameterizedTest
    @CsvSource({"ja, jp, ja_JP", "en, '', en", "'', UK, UK"})
    public void set(String language, String country, String expected) {
        provider.set(player, new Locale(language, country));
        
        verify(config).set(player.getUniqueId().toString(), expected);
    }
    
    
    @Test
    public void save() {
        provider.save();
        
        verify(config).save();
    }
    
}
