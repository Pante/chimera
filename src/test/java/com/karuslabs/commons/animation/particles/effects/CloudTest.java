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
package com.karuslabs.commons.animation.particles.effects;

import com.karuslabs.commons.animation.particles.ColouredParticles;
import com.karuslabs.commons.world.*;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.bukkit.Color.WHITE;
import static org.mockito.Mockito.*;


public class CloudTest {
    
    private ColouredParticles particles = new ColouredParticles(null, 1, WHITE);
    private ColouredParticles droplets = new ColouredParticles(null, 1, WHITE);
    private Cloud cloud = spy(new Cloud(particles, droplets));
    private Location location = new Location(null, 1, 1, 1);
    private StaticLocation origin = new StaticLocation(location, null, false);
    private StubContext<BoundLocation, BoundLocation> context = spy(new StubContext<>(origin, null, 0, 0));
    
    
    @Test
    public void render() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        doNothing().when(cloud).renderCloud(context, location, random, 50);
        doNothing().when(cloud).renderDroplets(context, location, random, 15);
        
        cloud.render(context);
        
        verify(cloud).renderCloud(context, location, random, 50);
        verify(cloud).renderDroplets(context, location, random, 15);
    }
    
    
    @Test
    public void renderCloud() {
        cloud.renderCloud(context, location, ThreadLocalRandom.current(), 1);
        
        verify(context).render(particles, location);
    }
    
    
    @ParameterizedTest
    @CsvSource({"0, 2", "1, 0"})
    public void renderDroplets(int number, int expected) {
        ThreadLocalRandom random = when(mock(ThreadLocalRandom.class).nextInt(2)).thenReturn(number).getMock();
        
        cloud.renderDroplets(context, location, random, 1);
        
        verify(context, times(expected)).render(droplets, location);
    }
    
}
