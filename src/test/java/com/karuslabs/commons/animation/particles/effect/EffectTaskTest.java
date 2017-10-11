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
package com.karuslabs.commons.animation.particles.effect;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.world.StaticLocation;

import java.util.function.BiConsumer;

import org.bukkit.Location;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.Mockito.*;


public class EffectTaskTest {
    
    private EffectTask<StaticLocation, StaticLocation> effect;
    private Task<Task, StaticLocation, StaticLocation> task;
    private BiConsumer<Particles, Location> consumer;
    private StaticLocation origin;
    private StaticLocation target;
    
    
    public EffectTaskTest() {
        task = mock(Task.class);
        consumer = mock(BiConsumer.class);
        origin = spy(new StaticLocation(new Location(null, 1, 2, 3), null, false));
        target = spy(new StaticLocation(new Location(null, 2, 3, 4), null, false));
        effect = spy(new EffectTask<>(task, consumer, origin, target, true, 0));
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, true, true, 1, 1, 0", "true, true, false, 1, 0, 0", "true, false, true, 0, 0, 1", "false, true, true, 0, 0, 1"})
    public void process(boolean origin, boolean target, boolean orientate, int times, int orientateTimes, int done) {
        doNothing().when(effect).orientate();
        doNothing().when(effect).done();
        
        doReturn(origin).when(this.origin).validate();
        doReturn(target).when(this.target).validate();
        effect.orientate = orientate;
        
        effect.process();
        
        verify(this.origin, times(times)).update();
        verify(this.target, times(times)).update();
        verify(effect, times(orientateTimes)).orientate();
        verify(task, times(times)).render(effect);
        verify(effect, times(done)).done();
    }
    
    
    @Test
    public void orientate() {
        effect.orientate();
        
        Location origin = effect.getOrigin().getLocation();
        Location target = effect.getTarget().getLocation();
        
        assertEquals(-35.26439, origin.getPitch(), 0.00001);
        assertEquals(315.0, origin.getYaw(), 0.00001);
        
        assertEquals(35.26439, target.getPitch(), 0.00001);
        assertEquals(135.0, target.getYaw(), 0.00001);
    }
    
    
    @Test
    public void render() {
        Particles particles = mock(Particles.class);
        Location location = new Location(null, 0, 0, 0);
        
        effect.render(particles, location);
        
        verify(consumer).accept(particles, location);
    }
    
}
