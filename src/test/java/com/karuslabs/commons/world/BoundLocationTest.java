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

import com.karuslabs.commons.world.BoundLocation.Builder;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class BoundLocationTest {
    
    private BoundLocation bound;
    private Location location;
    private DirectionalVector offset;
    
    
    public BoundLocationTest() {
        location = mock(Location.class);
        when(location.getYaw()).thenReturn(1F);
        when(location.getPitch()).thenReturn(2F);
        
        offset = new DirectionalVector(1, 2, 3, 4, 5);
        bound = spy(new StubBuilder(new StubLocation(location, null, false)).offset(offset).relative(true).build());
    }
    
    
    @Test
    public void addOffset() {
        bound.addOffset(new Vector(1, 1, 1), 1, 1);
        
        assertEquals(new DirectionalVector(2, 3, 4, 5, 6), bound.getOffset());
        verify(bound).updateOffset();
    }
    
    
    @Test
    public void setDirection() {
        Vector direction = new Vector(1, 1, 1);
        doNothing().when(bound).updateDirection();
        
        bound.setDirection(direction);
        
        verify(location).setDirection(direction);
        verify(bound).updateDirection();
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 5, 7", "false, 4, 5"})
    public void updateDirection(boolean relative, float yaw, float pitch) {
        bound.setRelative(relative);
        
        bound.updateDirection();
        
        verify(location).setYaw(yaw);
        verify(location).setPitch(pitch);
    }
    
    
    private static class StubBuilder extends Builder<StubBuilder, BoundLocation> {

        public StubBuilder(BoundLocation location) {
            super(location);
        }

        @Override
        protected StubBuilder getThis() {
            return this;
        }
        
    }
    
    private static class StubLocation extends BoundLocation {

        public StubLocation(Location location, DirectionalVector offset, boolean relative) {
            super(location, offset, relative);
        }

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void update() {

        }

        @Override
        public void updateOffset() {

        }
    };

}
