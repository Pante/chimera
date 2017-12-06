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
package com.karuslabs.commons.effect;

import com.karuslabs.commons.effect.Tasks.GlobalTask;
import com.karuslabs.commons.world.StaticLocation;

import org.bukkit.Location;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TaskTest {
    
    Effect effect = mock(Effect.class);
    Location location = new Location(null, 1, 2, 3);
    Runnable runnable = mock(Runnable.class);
    StaticLocation origin = spy(new StaticLocation(location, null, false));
    StaticLocation target = spy(new StaticLocation(new Location(null, 2, 3, 4), null, false));
    Task task = spy(new GlobalTask(effect, origin, target, true, 0) {
        @Override
        protected void done() {
            runnable.run();
        }
    });
    
    
    @ParameterizedTest
    @CsvSource({"true, true, true, 1, 1, 0", "true, true, false, 1, 0, 0", "true, false, true, 0, 0, 1", "false, true, true, 0, 0, 1"})
    void process(boolean origin, boolean target, boolean orientate, int times, int orientateTimes, int done) {
        doNothing().when(task).orientate();
        doNothing().when(task).render();
        doReturn(origin).when(this.origin).validate();
        doReturn(target).when(this.target).validate();
        
        task.orientate = orientate;
        
        task.process();
        
        verify(this.origin, times(times)).update();
        verify(this.target, times(times)).update();
        verify(task, times(orientateTimes)).orientate();
        verify(task, times(times)).render();
        verify(runnable, times(done)).run();
    }
    
    
    @Test
    void orientate() {
        task.orientate();
        
        Location origin = task.origin.getLocation();
        Location target = task.target.getLocation();
        
        assertEquals(-35.26439, origin.getPitch(), 0.00001);
        assertEquals(315.0, origin.getYaw(), 0.00001);
        
        assertEquals(35.26439, target.getPitch(), 0.00001);
        assertEquals(135.0, target.getYaw(), 0.00001);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, true, 1", "true, false, 0", "false, true, 11", "false, false, 10"})
    void render(boolean reset, boolean incremental, int expected) {
        task.steps = 10;
        
        when(effect.reset(anyInt())).thenReturn(reset);
        when(effect.isIncremental()).thenReturn(incremental);
        
        task.render();
        
        verify(effect).render(task, task.origin.getLocation(), task.target.getLocation(), task.vector);
        assertEquals(expected, task.steps);
    }
    
    
    @Test
    void steps() {
        task.steps = 5;
        
        task.steps(10);
        assertEquals(10, task.steps());
    }
    
    
    @Test
    void value() {
        assertNull(task.value());
    }
    
}
