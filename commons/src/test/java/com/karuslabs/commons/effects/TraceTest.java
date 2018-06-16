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
package com.karuslabs.commons.effects;

import java.util.stream.Stream;

import org.bukkit.*;
import org.bukkit.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class TraceTest extends EffectBase {
    
    private static final World WORLD = mock(World.class);
    
    Trace trace = spy(new Trace(PARTICLES).get());
    
    
    @ParameterizedTest
    @MethodSource("render_parameters")
    void render(World world, int rendered, int cancelled) {
        origin = new Location(WORLD, 0, 0, 0);
        trace.location.setWorld(world);
        
        doNothing().when(trace).render(context, origin);
        
        trace.render(context, origin, target, offset);
        
        verify(trace, times(rendered)).render(context, origin);
        verify(context, times(cancelled)).cancel();
    }
    
    static Stream<Arguments> render_parameters() {
        return Stream.of(
            of(null, 1, 0),
            of(WORLD, 1, 0),
            of(mock(World.class), 0, 1)
        );
    }
    
    
    @Test
    void render_location() {
        doReturn(new Vector(3, 3, 3)).when(trace).process(origin);
        context.steps = 4;
        
        trace.render(context, origin);
        
        verify(context).render(PARTICLES, trace.location);
        assertVector(from(3, 3, 3), context.captured);
    }
    
    
    @ParameterizedTest
    @CsvSource({"2, 1, 1, 1, 1", "1, 3, 3, 3, 0"})
    void process(int max, int x, int y, int z, int size) {
        trace.waypoints.add(new Vector(3, 3, 3));
        trace.max = max;
        
        assertVector(from(1, 1, 1), trace.process(origin));
        assertEquals(size, trace.waypoints.size());
    }
    
}
