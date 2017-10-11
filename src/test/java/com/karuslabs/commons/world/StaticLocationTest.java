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
package com.karuslabs.commons.world;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class StaticLocationTest {
    
    private Location internal;
    private PathVector offset;
    private StaticLocation location;
        
            
    public StaticLocationTest() {
        internal = mock(Location.class);
        when(internal.getYaw()).thenReturn(1F);
        when(internal.getPitch()).thenReturn(2F);
        
        offset = new PathVector(1, 2, 3, 4, 5);
        
        location = StaticLocation.builder(internal).offset(offset).build();
    }
    
    
    @Test
    public void validate() {
        assertTrue(location.validate());
    }
    
    
    @ParameterizedTest
    @CsvSource({"true", "false"})
    public void updateOffset(boolean relative) {
        location.setRelative(relative);
        
        location.updateOffset();
        
        verify(internal).add(relative ? Vectors.rotate(offset, internal) : offset);
    }
    
}
